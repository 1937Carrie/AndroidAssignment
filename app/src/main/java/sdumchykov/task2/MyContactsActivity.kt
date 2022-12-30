package sdumchykov.task2

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

class MyContactsActivity : AppCompatActivity() {
    private val contactsModeTumbler = false
    private val TAG = "MyContactsActivity"
    private lateinit var binding: ActivityMyContactsBinding
    private lateinit var viewModel: ContactViewModel
    private lateinit var contactList: ArrayList<Contact>
    private val adapter = ItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                MyViewModelFactory(Datasource.get())
            )[ContactViewModel::class.java]
        }

        viewModel.contactList.observe(this) {
            Log.d(TAG, "onCreate: $it")
            adapter.setContactList(it)
        }
        viewModel.getAllContacts()

        if (contactsModeTumbler) {
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

        binding.textViewContacts.setOnClickListener {
            val contactList1 = Datasource.get()
            contactList1[0].name = "Changed text"
            Datasource.set(contactList1)

            viewModel.getAllContacts()
        }
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