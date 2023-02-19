package sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.presentation.viewPager.contacts.MyContactsViewModel

class FetchContacts(private val parentViewModel: MyContactsViewModel) {

    fun fetchContacts(activity: Activity, onSuccess: () -> Unit) {
        Dexter.withActivity(activity).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
//                        fetchPhoneContacts(activity)
                        onSuccess()
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
    private fun fetchPhoneContacts(context: Context) {
        val phones = context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        if (phones != null) {
            val userModels = ArrayList<UserModel>()
            while (phones.moveToNext()) {
                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact =
                    UserModel(userModels.size, name, phoneNumber, Constants.HARDCODED_IMAGE_URL)

                userModels.add(contact)
            }
            parentViewModel.addData(userModels)
            phones.close()
        }
    }
}
