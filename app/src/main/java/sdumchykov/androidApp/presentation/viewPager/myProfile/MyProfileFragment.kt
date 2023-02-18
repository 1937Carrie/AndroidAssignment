package sdumchykov.androidApp.presentation.viewPager.myProfile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.presentation.MainActivityArgs
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    val parentViewModel by lazy { pagerFragment.myContactsViewModel }
    private val args: MainActivityArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        handleRecyclerViewContent() // TODO якщо прибрати тут або на :115, то працювати не буде
    }

    override fun setListeners() {
        imageViewMainProfilePictureSetOnClickListener()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image
        binding.imageViewMainProfilePicture.setImage(drawableSource)
    }

    private fun setTextToTextName() {
        var receivedEmail =
            requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .getString(EMAIL_KEY, "")
                .toString()

        if (receivedEmail == "") {
            receivedEmail = args.email

            cacheEmailToSharedPreferences()
        }

        val splittedEmail = receivedEmail.substring(0, receivedEmail.indexOf(SIGN_AT))
            .split(Regex(PATTERN_NON_CHARACTER))
        binding.textViewMainName.text = if (splittedEmail.size > 1) {
            val firstName = splittedEmail[0].replaceFirstChar { it.uppercase() }
            val secondName = splittedEmail[1].replaceFirstChar { it.uppercase() }
            val textContent = "$firstName $secondName"
            textContent
        } else {
            receivedEmail.substring(0, receivedEmail.indexOf(SIGN_AT))
        }
    }

    private fun cacheEmailToSharedPreferences() {
        val cachedData =
            this.requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val editor = cachedData.edit()

        editor.putString(EMAIL_KEY, args.email)

        editor.apply()
    }

    private fun setURIToImageInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun imageViewMainProfilePictureSetOnClickListener() {
        binding.imageViewMainProfilePicture.setOnClickListener {
//            activity?.viewModelStore.clear()

            val fetchContactList = !viewModel.getFetchContactList()
            viewModel.setFetchContactList(fetchContactList)

            val toastText = if (fetchContactList) SHOW_CONTACT_LIST
            else SHOW_HARDCODED_LIST
            Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()

            handleRecyclerViewContent()
            if (!parentViewModel.getFetchContactList()) parentViewModel.initContactList()
        }
    }

    private fun handleRecyclerViewContent() {
        if (parentViewModel.getFetchContactList()) getContactsListWithDexter()
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
                    permission: PermissionRequest,
                    token: PermissionToken
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
                        phones.getString(
                            phones.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                            )
                        )
                    val phoneNumber =
                        phones.getString(
                            phones.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                    val contact =
                        UserModel(userModels.size, name, phoneNumber, Constants.HARDCODED_IMAGE_URL)

                    userModels.add(contact)
                }
                parentViewModel.addData(userModels)
                phones.close()
            }
        }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem = 1
        }
    }
}
