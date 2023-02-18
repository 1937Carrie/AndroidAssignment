package sdumchykov.androidApp.presentation.viewPager.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.AddContactsDialogBinding
import sdumchykov.androidApp.databinding.FragmentMyContactsBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.SwipeToDeleteCallback
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragmentDirections
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.UsersAdapter
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener

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
                    imageViewMyContactsCancelSetOnClickListener()
                }

                override fun onContactSelectedStateChanged() {
                    if (usersAdapter.areAllItemsUnselected()) {
                        disableMultiselectViewState()
                        parentViewModel.selectedEvent.value = false
                        enableContactSwipe()
                        initRecyclerView()
                    }
                }

                // TODO я хочу цю функцію винести безпосередньо у клас, а не тримати її у анонімному об'єкті, але тоді нема доступу до onContactSelectedStateChanged(). Як жити?
                private fun imageViewMyContactsCancelSetOnClickListener() {
                    binding.imageViewMyContactsCancel?.setOnClickListener {
                        usersAdapter.unselectItems()
                        onContactSelectedStateChanged()
                    }
                }
            }
        )
    }
    private var swipeFlags = ItemTouchHelper.END
    private lateinit var dialogAddContact: AlertDialog

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
    }

    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageViewMyContactsArrowBack.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem = 0
        }
    }

    private fun multiSelectedItemsTrashCanOnClickListener() {
        binding.buttonRemoveSelectedContacts?.setOnClickListener {
            removeSelectedItemsFromRecyclerView()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyContactsContactList.run {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            ) // TODO first parameter context or requireContext() https://stackoverflow.com/a/60402600/20937549
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
            "${contact.name} has been deleted",
            SNACKBAR_TIME_LENGTH
        )

        snackbar.setAction("Undo") {
            parentViewModel.addItem(contact, index)

            Toast.makeText(
                activity, "${contact.name} has been restored", Toast.LENGTH_LONG
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

            dialogAddContact =
                AlertDialog.Builder(requireActivity()).setView(addContactsBinding.root)
                    .setOnCancelListener {
                        clearFieldsInDialog(name, surname, profession)
                    }.create()

            buttonAddContactsDialogAdd.setOnClickListener {
                parentViewModel.addNewItem(
                    name = "${name.text} ${surname.text}", profession = profession.text.toString()
                )

                clearFieldsInDialog(name, surname, profession)
                dialogAddContact.dismiss()
            }

            buttonAddContactsDialogCancel.setOnClickListener {
                clearFieldsInDialog(name, surname, profession)
                dialogAddContact.dismiss()
            }

            binding.textViewMyContactsAddContacts.setOnClickListener { dialogAddContact.show() }
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
        if (parentViewModel.getFetchContactList()) getContactsListWithDexter()
    }

    private fun getContactsListWithDexter() {
        Dexter.withActivity(activity).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
                        fetchContacts()
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        activity, "Permission should be granted!", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    @SuppressLint("Range")
    private fun fetchContacts() {
        val phones = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        if (phones != null) {
            val userModels = ArrayList<UserModel>()
            while (phones.moveToNext()) {
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact = UserModel(userModels.size, name, phoneNumber, HARDCODED_IMAGE_URL)

                userModels.add(contact)
            }
            parentViewModel.addData(userModels)
            phones.close()
        }
    }

    //endregion
    private fun enableMultiselectViewState() {
        with(binding) {
            frameLayoutButtonsContainer?.visibility = View.VISIBLE
            buttonRemoveSelectedContacts?.visibility = View.VISIBLE
            textViewMyContactsAddContacts.visibility = View.GONE
            imageViewMyContactsArrowBack.visibility = View.GONE
            imageViewMyContactsCancel?.visibility = View.VISIBLE
        }
    }

    private fun disableMultiselectViewState() {
        with(binding) {
            frameLayoutButtonsContainer?.visibility = View.GONE
            buttonRemoveSelectedContacts?.visibility = View.GONE
            textViewMyContactsAddContacts.visibility = View.VISIBLE
            imageViewMyContactsArrowBack.visibility = View.VISIBLE
            imageViewMyContactsCancel?.visibility = View.GONE
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
            binding.frameLayoutButtonsContainer?.visibility = View.GONE
            binding.buttonRemoveSelectedContacts?.visibility = View.GONE
            binding.imageViewMyContactsCancel?.visibility = View.GONE
            binding.imageViewMyContactsArrowBack.visibility = View.VISIBLE
            binding.textViewMyContactsAddContacts.visibility = View.VISIBLE
        }
    }
}
