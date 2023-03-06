package sdumchykov.androidApp.presentation.viewPager.myProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.local.User
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragmentDirections
import sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts.FetchContacts

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private lateinit var credentials: User

    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val parentViewModel by lazy { pagerFragment.contactsViewModel }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCredentials()

        checkPrelaunchPermissions()

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
    }


    override fun setListeners() {
        imageViewMainProfilePictureSetListener()
        editProfileSetListener()
        buttonViewMyContactsSetOnClickListener()
    }

    override fun setObservers() {
        myProfileViewModel.userLiveData.observe(this) {
            credentials = it
        }
    }

    private fun checkPrelaunchPermissions() {
        if (parentViewModel.userLiveData.value?.isEmpty() == true)
            if (!isPermissionsGranted()) {
                myProfileViewModel.setFetchContactList(false)
                parentViewModel.apiGetUserContacts()
            } else {
                if (myProfileViewModel.getFetchContactList()) parentViewModel.initRealUsersList()
                else parentViewModel.apiGetUserContacts()
            }
    }

    private fun isPermissionsGranted(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun initCredentials() {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        val userDao = db.userDao()
        credentials = userDao.getUser()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image_girl
        binding.imageViewMainProfilePicture.setImage(drawableSource)
    }

    private fun setTextToTextName() {
//        var receivedEmail = credentialsViewModel.credentialsLiveData.value?.email ?: ""

        val receivedEmail = credentials.email ?: ""

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

//        if (signUpViewModel.getPassword() == "") {
//            signUpViewModel.saveEmail("")
//        }
    }

    private fun setURIToImageInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun imageViewMainProfilePictureSetListener() {
        binding.imageViewMainProfilePicture.setOnClickListener {
            val fetchContactList = !myProfileViewModel.getFetchContactList()
            myProfileViewModel.setFetchContactList(fetchContactList)

            if (fetchContactList) {
                if (!isPermissionsGranted()) {
                    FetchContacts().fetchContacts(activity as AppCompatActivity,
                        { parentViewModel.initRealUsersList() },
                        { parentViewModel.apiGetUserContacts() })
                    if (!isPermissionsGranted()) myProfileViewModel.setFetchContactList(false)
                } else parentViewModel.initRealUsersList()

            } else parentViewModel.apiGetUserContacts()

            val toastText = if (myProfileViewModel.getFetchContactList()) SHOW_CONTACT_LIST
            else SHOW_HARDCODED_LIST
            Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    private fun editProfileSetListener() {
        binding.buttonMainEditProfile.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToEditProfileFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem =
                ViewPagerFragment.Tabs.CONTACTS.ordinal
        }
    }
}
