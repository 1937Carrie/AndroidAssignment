package sdumchykov.androidApp.presentation.viewPager.contacts

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.AddContactsDialogBinding
import sdumchykov.androidApp.databinding.FragmentMyContactsBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.SwipeToDeleteCallback
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.visible
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragmentDirections
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.UsersAdapter
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts.FetchContacts

private const val SNACKBAR_TIME_LENGTH = 5000

@AndroidEntryPoint
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val parentViewModel by lazy { pagerFragment.myContactsViewModel }
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(
            usersListener = object : UsersListener {
                override fun onUserClickAction(userModel: UserModel, adapterPosition: Int) {
                    Log.d("MainActivity", "onUserClickAction: ${userModel.id}")
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToContactProfileFragment(
                            userModel
                        )
                    Navigation.findNavController(binding.root).navigate(action)
                }

                override fun onTrashIconClickAction(userModel: UserModel, userIndex: Int) {
                    deleteContact(userModel, userIndex)
                }

                override fun onContactRemove(userModel: UserModel) {
                    parentViewModel.removeItem(userModel)
                }

                override fun onMultiselectActivated() {
                    usersAdapter.enableMultiselect()
                    enableMultiselectViewState()
                    parentViewModel.selectedEvent.value = true
                    disableContactSwipe()
                    initRecyclerView()
                }

                override fun onContactSelectedStateChanged() {
                    if (usersAdapter.areAllItemsUnselected()) {
                        disableMultiselectViewState()
                        parentViewModel.selectedEvent.value = false
                        enableContactSwipe()
                        initRecyclerView()
                    }
                }
            }
        )
    }

    private var swipeFlags = ItemTouchHelper.END
    private var dialogAddContact: AlertDialog? = null

    //region Main code
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setSwipeToDelete()
        createAddContactDialog()
        handleRecyclerViewContent()
    }

    override fun setObservers() {
        parentViewModel.userLiveData.observe(this) { users ->
            usersAdapter.submitList(users.toMutableList())
        }
    }

    override fun setListeners() {
        imageButtonArrowBackSetOnClickListener()
        multiSelectedItemsTrashCanOnClickListener()
        imageViewMyContactsCancelSetOnClickListener()
    }

    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageViewMyContactsArrowBack.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem = 0
        }
    }

    private fun multiSelectedItemsTrashCanOnClickListener() {
        binding.buttonRemoveSelectedContacts.setOnClickListener {
            removeSelectedItemsFromRecyclerView()
        }
    }

    private fun imageViewMyContactsCancelSetOnClickListener() {
        binding.imageViewMyContactsCancel?.setOnClickListener {
            usersAdapter.unselectItems()
            usersAdapter.usersListener.onContactSelectedStateChanged()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyContactsContactList.run {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun setSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = parentViewModel.getContactByPosition(position)
                contact?.let {
                    deleteContact(it, position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMyContactsContactList)
    }

    fun deleteContact(contact: UserModel, index: Int) {
        parentViewModel.removeItem(contact)

        val snackbar = Snackbar.make(
            binding.recyclerViewMyContactsContactList,
            getString(R.string.contactHasBeenDeleted, contact.name),
            SNACKBAR_TIME_LENGTH
        )

        snackbar.setAction(getString(R.string.undo)) {
            parentViewModel.addItem(contact, index)

            Toast.makeText(
                activity,
                getString(R.string.contactHasBeenRestored, contact.name),
                Toast.LENGTH_LONG
            ).show()
        }
        snackbar.show()
    }

    private fun createAddContactDialog() {
        val addContactsBinding = AddContactsDialogBinding.inflate(layoutInflater)

        with(addContactsBinding) {
            val name = editTextAddContactsDialogName
            val surname = editTextAddContactsDialogSurname
            val profession = editTextAddContactsDialogProfession

            dialogAddContact = activity?.let {
                AlertDialog.Builder(it).setView(addContactsBinding.root).setOnCancelListener {
                    clearFieldsInDialog(name, surname, profession)
                }.create()
            }

            buttonAddContactsDialogAdd.setOnClickListener {
                parentViewModel.addNewItem(
                    name = "${name.text} ${surname.text}", profession = profession.text.toString()
                )

                clearFieldsInDialog(name, surname, profession)
                dialogAddContact?.dismiss()
            }

            buttonAddContactsDialogCancel.setOnClickListener {
                clearFieldsInDialog(name, surname, profession)
                dialogAddContact?.dismiss()
            }

            binding.textViewMyContactsAddContacts.setOnClickListener { dialogAddContact?.show() }
        }
    }

    private fun clearFieldsInDialog(
        name: AppCompatEditText?,
        surname: AppCompatEditText?,
        profession: AppCompatEditText?
    ) {
        name?.text?.clear()
        surname?.text?.clear()
        profession?.text?.clear()
    }

    private fun handleRecyclerViewContent() {
        if (parentViewModel.getFetchContactList()) fetchPhoneContacts()
    }

    private fun fetchPhoneContacts() {
        val fetchContacts = FetchContacts(parentViewModel)
        fetchContacts.fetchContacts(activity as AppCompatActivity)
    }

    //endregion
    private fun enableMultiselectViewState() {
        with(binding) {
            frameLayoutButtonsContainer.visible()
            buttonRemoveSelectedContacts.visible()
            textViewMyContactsAddContacts.gone()
            imageViewMyContactsArrowBack.gone()
            imageViewMyContactsCancel?.visible()
        }
    }

    private fun disableMultiselectViewState() {
        with(binding) {
            frameLayoutButtonsContainer.gone()
            buttonRemoveSelectedContacts.gone()
            textViewMyContactsAddContacts.visible()
            imageViewMyContactsArrowBack.visible()
            imageViewMyContactsCancel?.gone()
        }
    }

    private fun enableContactSwipe() {
        swipeFlags = ItemTouchHelper.END
    }

    private fun disableContactSwipe() {
        swipeFlags = 0
    }

    private fun removeSelectedItemsFromRecyclerView() {
        usersAdapter.removeSelectedItems(parentViewModel)
        initRecyclerView()
        if (parentViewModel.userLiveData.value?.isEmpty() == true) {
            binding.frameLayoutButtonsContainer.gone()
            binding.buttonRemoveSelectedContacts.gone()
            binding.textViewMyContactsAddContacts.visible()
            binding.imageViewMyContactsArrowBack.visible()
            binding.imageViewMyContactsCancel?.gone()
        }
    }
}
