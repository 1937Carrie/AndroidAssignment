package sdumchykov.androidApp.presentation.viewPager.contacts

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyContactsBinding
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.SwipeToDeleteCallback
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.visible
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragmentDirections
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.UsersAdapter
import sdumchykov.androidApp.presentation.viewPager.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.viewPager.myProfile.MyProfileViewModel

private const val SNACKBAR_TIME_LENGTH = 5000

@AndroidEntryPoint
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val parentViewModel by lazy { pagerFragment.contactsViewModel }
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(
            usersListener = object : UsersListener {
                override fun onUserClickAction(user: User, adapterPosition: Int) {
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToContactProfileFragment(
                            user
                        )
                    Navigation.findNavController(binding.root).navigate(action)
                }

                override fun onContactRemove(user: User) {
                    deleteContact(user)
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

    //region Main code
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setSwipeToDelete()
    }

    override fun onResume() {
        super.onResume()
        if (!myProfileViewModel.getFetchContactList()) parentViewModel.apiGetUserContacts()
    }

    override fun setObservers() {
        setUserLiveDataObserver()
    }

    override fun setListeners() {
        imageButtonArrowBackSetListener()
        searchViewSetListener()
        iconSearchSetListener()
        multiSelectedItemsTrashCanSetListener()
        imageViewMyContactsCancelSetListener()
        textViewAddContactsSetListener()
    }

    private fun setUserLiveDataObserver() {
        parentViewModel.userLiveData.observe(this) { users ->
            usersAdapter.submitList(users.toMutableList())
        }
    }

    private fun imageButtonArrowBackSetListener() {
        binding.imageViewMyContactsArrowBack.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem = 0
        }
    }

    private fun searchViewSetListener() {
        binding.searchViewMyContacts?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
        binding.searchViewMyContacts?.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                with(binding) {
                    imageViewMyContactsArrowBack.visible()
                    textViewMyContactsContacts.visible()
                    imageViewMyContactsSearch.visible()

                    searchViewMyContacts?.gone()
                    usersAdapter.submitList(parentViewModel.userLiveData.value?.toMutableList())
                }

                return true
            }
        })
    }

    private fun iconSearchSetListener() {
        binding.imageViewMyContactsSearch.setOnClickListener {
            with(binding) {
                imageViewMyContactsArrowBack.gone()
                textViewMyContactsContacts.gone()
                imageViewMyContactsSearch.gone()

                searchViewMyContacts?.visible()
            }
        }
    }

    private fun filterList(text: String?) {
        if (text != null) {
            val filteredList = arrayListOf<User>()

            parentViewModel.userLiveData.value.let {
                if (it != null) {
                    for (item in it) {
                        if (item.name?.lowercase()?.contains(text.lowercase()) == true) {
                            filteredList.add(item)
                        }
                    }
                }
            }

            if (filteredList.isEmpty()) {
                with(binding) {
                    recyclerViewMyContactsContactList.gone()
                    textViewMyContactsTopText?.visible()
                    textViewMyContactsBottomText?.visible()
                }

            } else {
                with(binding) {
                    recyclerViewMyContactsContactList.visible()
                    textViewMyContactsTopText?.gone()
                    textViewMyContactsBottomText?.gone()
                }
                usersAdapter.submitList(filteredList.toMutableList())
            }
        }
    }

    private fun multiSelectedItemsTrashCanSetListener() {
        binding.buttonRemoveSelectedContacts.setOnClickListener {
            removeSelectedItemsFromRecyclerView()
        }
    }

    private fun imageViewMyContactsCancelSetListener() {
        binding.imageViewMyContactsCancel?.setOnClickListener {
            usersAdapter.unselectItems()
            usersAdapter.usersListener.onContactSelectedStateChanged()
        }
    }

    private fun textViewAddContactsSetListener() {
        with(binding) {
            textViewMyContactsAddContacts.setOnClickListener {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactFragment()
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMyContactsContactList.run {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = parentViewModel.getContactByPosition(position)
                contact?.let {
                    deleteContact(it)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMyContactsContactList)
    }

    fun deleteContact(user: User) {
        parentViewModel.apiDeleteContact(user.id)

        val snackbar = Snackbar.make(
            binding.recyclerViewMyContactsContactList,
            getString(R.string.contactHasBeenDeleted, user.name),
            SNACKBAR_TIME_LENGTH
        )

        snackbar.setAction(getString(R.string.undo)) {
            parentViewModel.apiAddContact(user.id)
            parentViewModel.apiGetUserContacts()

            Toast.makeText(
                activity,
                getString(R.string.contactHasBeenRestored, user.name),
                Toast.LENGTH_LONG
            ).show()
        }
        snackbar.show()

        parentViewModel.apiGetUserContacts()
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
        usersAdapter.removeSelectedItems(parentViewModel, myProfileViewModel.getFetchContactList())

        if (parentViewModel.userLiveData.value?.isEmpty() == true) {
            with(binding) {
                frameLayoutButtonsContainer.gone()
                buttonRemoveSelectedContacts.gone()
                textViewMyContactsAddContacts.visible()
                imageViewMyContactsArrowBack.visible()
                imageViewMyContactsCancel?.gone()
            }
        }
    }
}
