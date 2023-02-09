package sdumchykov.androidApp.presentation.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.activityViewModels
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
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyContactsBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants.HARDCODED_IMAGE_URL
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.contacts.adapter.UsersAdapter
import sdumchykov.androidApp.presentation.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.utils.SwipeToDeleteCallback

private const val SNACKBAR_DURATION = 5000

@AndroidEntryPoint
class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {

    private val myContactsViewModel: MyContactsViewModel by activityViewModels()
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(usersListener = object : UsersListener {
            override fun onUserClickAction(userModel: UserModel, adapterPosition: Int) {
                Log.d("MainActivity", "onUserClickAction: ${userModel.id}")
                val action =
                    MyContactsFragmentDirections.actionMyContactsFragmentToContactProfileFragment(
                        adapterPosition
                    )
                Navigation.findNavController(binding.root).navigate(action)
            }

            override fun onTrashIconClickAction(userModel: UserModel, userIndex: Int) {
                deleteContact(userModel, userIndex)
            }
        })
    }
    private lateinit var dialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclers()
        setSwipeToDelete()
        createAddContactDialog()
        handleRecyclerViewContent()
    }

    override fun setObservers() {
        myContactsViewModel.userLiveData.observe(this) { users ->
            usersAdapter.submitList(users.toMutableList())
        }
    }

    override fun setListeners() {
        imageButtonArrowBackSetOnClickListener()
    }


    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageViewMyContactsArrowBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_myContactsFragment_to_myProfileFragment)
        }
    }

    private fun initRecyclers() {
        binding.recyclerViewMyContactsContactList.run {
            adapter = usersAdapter
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = myContactsViewModel.getContactByPosition(position)
                contact?.let {
                    deleteContact(it, position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMyContactsContactList)
    }

    fun deleteContact(contact: UserModel, index: Int) {
        myContactsViewModel.removeItem(contact)

        val snackbar = Snackbar.make(
            binding.recyclerViewMyContactsContactList,
            "${contact.name} has been deleted",
            SNACKBAR_DURATION
        )

        snackbar.setAction("Undo") {
            myContactsViewModel.addItem(contact, index)

            Toast.makeText(
                activity, "${contact.name} has been restored", Toast.LENGTH_LONG
            ).show()

        }
        snackbar.show()
    }

    private fun createAddContactDialog() {
        val view = layoutInflater.inflate(R.layout.add_contacts_dialog, null)

        val name = view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogName)
        val surname = view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogSurname)
        val profession =
            view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogProfession)

        dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setOnCancelListener {
                clearFieldsInDialog(name, surname, profession)
            }.create()

        val buttonAddContacts: AppCompatButton = view.findViewById(R.id.buttonAddContactsDialogAdd)
        buttonAddContacts.setOnClickListener {
            myContactsViewModel.addNewItem(
                name = "${name.text.toString()} ${surname.text.toString()}",
                profession = profession.text.toString()
            )

            clearFieldsInDialog(name, surname, profession)

            dialog.dismiss()
        }
        val buttonCancel: AppCompatButton = view.findViewById(R.id.buttonAddContactsDialogCancel)
        buttonCancel.setOnClickListener {
            clearFieldsInDialog(name, surname, profession)

            dialog.dismiss()
        }

        binding.textViewMyContactsAddContacts.setOnClickListener { dialog.show() }
    }

    private fun clearFieldsInDialog(
        name: AppCompatEditText?, surname: AppCompatEditText?, profession: AppCompatEditText?
    ) {
        name?.text?.clear()
        surname?.text?.clear()
        profession?.text?.clear()
    }

    private fun handleRecyclerViewContent() {
        if (myContactsViewModel.getFetchContactList() && myContactsViewModel.userLiveData.value?.size == 0) getContactsListWithDexter()
    }

    private fun getContactsListWithDexter() {
        Dexter.withActivity(activity).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
                        contacts
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        activity, "Permission should be granted!", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest, token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private val contacts: Unit
        @SuppressLint("Range") get() {
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
                myContactsViewModel.addData(userModels)
                phones.close()
            }
        }

}