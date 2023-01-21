package sdumchykov.task3

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import sdumchykov.task3.adapter.ItemAdapter
import sdumchykov.task3.data.Datasource
import sdumchykov.task3.databinding.FragmentMyContactsBinding
import sdumchykov.task3.model.Contact
import sdumchykov.task3.model.ContactViewModel
import sdumchykov.task3.model.MyViewModelFactory

private const val HARDCODED_PHOTO_URL = "https://picsum.photos/200"
private const val FIVE_SECOND = 5000

class MyContactsFragment :
    BaseFragment<FragmentMyContactsBinding>(FragmentMyContactsBinding::inflate) {
    private val contactsModeTumbler = false
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactList: ArrayList<Contact>
    private val adapter = ItemAdapter(::deleteContact)
    private lateinit var dialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageButtonArrowBackSetOnClickListener()

        binding.recyclerViewContacts.adapter = adapter

        adapter.onItemClick = {
            Navigation.findNavController(binding.root).navigate(R.id.action_myContactsFragment_to_contactProfileFragment)
        }

        contactList = if (contactsModeTumbler) ArrayList() else Datasource().get()
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(contactList))[ContactViewModel::class.java]
        viewModel.contactList.observe(viewLifecycleOwner) { adapter.setContactList(it) }

        if (contactsModeTumbler) {
            getContactsListWithDexter()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = viewModel.contactList.value?.get(position)

                deleteContact(contact!!)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)

        createAddContactDialog()

        binding.textViewAddContacts.setOnClickListener { dialog.show() }

    }

    private fun createAddContactDialog() {
        val view = layoutInflater.inflate(R.layout.add_contacts_dialog, null)
        dialog = AlertDialog.Builder(activity).setView(view).create()

        val button: AppCompatButton = view.findViewById(R.id.button_addcontactsdialog_add)
        button.setOnClickListener {
            val name =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_name)
            val surname =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_surname)
            val profession =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_profession)

            viewModel.addItem(
                Contact(
                    "${name.text.toString()} ${surname.text.toString()}",
                    profession.text.toString(),
                    HARDCODED_PHOTO_URL
                )
            )

            name.text?.clear()
            surname.text?.clear()
            profession.text?.clear()

            dialog.dismiss()
        }
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
        @SuppressLint("NotifyDataSetChanged", "Recycle") get() {
            var phones: Cursor? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                phones = activity?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null
                )
            }

            while (phones!!.moveToNext()) {
                @SuppressLint("Range")
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                @SuppressLint("Range")
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact = Contact(name, phoneNumber, HARDCODED_PHOTO_URL)
                contactList.add(contact)
            }

        }

    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageButtonArrowBack.setOnClickListener {
//            activity?.onBackPressed() //plain back pressed action
            Navigation.findNavController(binding.root).navigate(R.id.action_myContactsFragment_to_myProfileFragment)
        }
    }

    private fun deleteContact(contact: Contact) {
        viewModel.removeItem(contact)

        val snackbar =
            Snackbar.make(
                binding.recyclerViewContacts, "${contact.name} has been deleted",
                FIVE_SECOND
            )

        snackbar.setAction("Undo") {
            viewModel.addItem(contact)
            Toast.makeText(
                activity, "${contact.name} has been restored", Toast.LENGTH_LONG
            ).show()
        }
        snackbar.show()
    }

}