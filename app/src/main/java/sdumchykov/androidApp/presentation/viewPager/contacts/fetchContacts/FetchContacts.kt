package sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts

import android.Manifest
import android.app.Activity
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class FetchContacts {

    fun fetchContacts(
        activity: Activity,
        functionOK: () -> Unit,
        functionNotOK: () -> Unit
    ) {
        Dexter.withActivity(activity).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
                        functionOK
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    functionNotOK
                    Toast.makeText(
                        activity,
                        "Permission should be granted!",
                        Toast.LENGTH_SHORT
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
}
