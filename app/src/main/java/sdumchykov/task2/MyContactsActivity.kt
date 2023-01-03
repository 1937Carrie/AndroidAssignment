package sdumchykov.task2

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import sdumchykov.task2.adapter.ItemAdapter
import sdumchykov.task2.data.Datasource
import sdumchykov.task2.databinding.ActivityMyContactsBinding
import sdumchykov.task2.model.Contact
import sdumchykov.task2.model.ContactViewModel
import sdumchykov.task2.model.MyViewModelFactory

class MyContactsActivity :
    BaseActivity<ActivityMyContactsBinding>(ActivityMyContactsBinding::inflate) {
    private val contactsModeTumbler = false
    private val TAG = "MyContactsActivity"
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactList: ArrayList<Contact>
    private val adapter = ItemAdapter(::deleteContact)
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageButtonArrowBackSetOnClickListener()

        binding.recyclerViewContacts.adapter = adapter

        if (contactsModeTumbler) {
            contactList = ArrayList()

            viewModel = ViewModelProvider(
                this,
                MyViewModelFactory(contactList)
            )[ContactViewModel::class.java]
        } else {
            viewModel = ViewModelProvider(
                this,
                MyViewModelFactory(Datasource().get())
            )[ContactViewModel::class.java]
        }

        viewModel.contactList.observe(this) {
            Log.d(TAG, "onCreate: $it")
            adapter.setContactList(it)
        }


        if (contactsModeTumbler) {
            getContactsListWithDexter()
        }

        binding.textViewContacts.setOnClickListener {
            viewModel.updateItem(
                0,
                Contact("Changed text", "Photograph", "https://picsum.photos/200")
            )
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
        binding.textViewAddContacts.setOnClickListener {
            dialog.show()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun createAddContactDialog() {
        val view = layoutInflater.inflate(R.layout.add_contacts_dialog, null)
        dialog = AlertDialog.Builder(this).setView(view).create()
        val button: AppCompatButton = view.findViewById(R.id.button_addcontactsdialog_add)
        button.setOnClickListener {
            val name =
                view.findViewById<TextInputEditText>(R.id.textinputedittext_addcontactsdialog_name).text.toString()
            val surname =
                view.findViewById<TextInputEditText>(R.id.textinputedittext_addcontactsdialog_surname).text.toString()
            val profession =
                view.findViewById<TextInputEditText>(R.id.textinputedittext_addcontactsdialog_profession).text.toString()
            viewModel.addItem(
                Contact(
                    "$name $surname",
                    profession,
                    "https://picsum.photos/200"
                )
            )
        }

    }

    private fun getContactsListWithDexter() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
                        contacts
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@MyContactsActivity,
                        "Permission should be granted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest, token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }

            }).check()
    }

    private fun deleteContact(contact: Contact) {
        viewModel.removeItem(contact)

        val snackbar =
            Snackbar.make(binding.recyclerViewContacts, "${contact.name} has been deleted", 5000)

        snackbar.setAction("Undo") {
            viewModel.addItem(contact)
            Toast.makeText(
                applicationContext, "${contact.name} has been restored", Toast.LENGTH_LONG
            ).show()
        }
        snackbar.show()
    }

    private val contacts: Unit
        @SuppressLint("NotifyDataSetChanged", "Recycle") get() {
            var phones: Cursor? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                phones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null
                )
            }

            while (phones!!.moveToNext()) {
                @SuppressLint("Range") val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                @SuppressLint("Range") val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val contact = Contact(name, phoneNumber, "https://picsum.photos/200")
                contactList.add(contact)
            }

        }

    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageButtonArrowBack.setOnClickListener {
            finish()
        }
    }
}