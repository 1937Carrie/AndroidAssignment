package sdumchykov.androidApp.presentation.viewPager.myProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.local.User
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.utils.ext.showToast
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

        fillOutProfile()
    }


    override fun setListeners() {
        imageViewMainProfilePictureSetListener()
        editProfileSetListener()
        buttonViewMyContactsSetOnClickListener()
    }

    override fun setObservers() {
        setUsersObserver()
        setStatusObserver()
    }

    private fun setUsersObserver() {
        myProfileViewModel.userLiveData.observe(viewLifecycleOwner) {
            credentials = it
        }
    }

    private fun setStatusObserver() {
        parentViewModel.statusUserContacts.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response.status) {
                    Status.SUCCESS -> {}
                    Status.ERROR -> {
                        showToast(requireContext(), "Failed to pull account contact list")
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun initCredentials() {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        val userDao = db.userDao()
        credentials = userDao.getUser()
    }

    private fun checkPrelaunchPermissions() {
        if (parentViewModel.userContacts.value?.isEmpty() == true)
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

    private fun fillOutProfile() {
        setMainPicture()
        setTextToName()
        setTextToProfession()
        setTextToAddress()
        setLinksToSocialNetworks()
    }

    private fun setMainPicture() {
        val image = credentials.image ?: Constants.STUB_IMAGE_URI

        binding.imageViewMainProfilePicture.setImage(Uri.parse(image))
    }

    private fun setTextToName() {
        binding.textViewMainName.text = credentials.name
    }

    private fun setTextToProfession() {
        binding.textViewMainProfession.text = credentials.career
    }

    private fun setTextToAddress() {
        binding.textViewMainAddress.text = credentials.address
    }

    private fun setLinksToSocialNetworks() {
        setLinkToFacebook()
        setLinkToLinkedIn()
        setLinkToInstagram()
    }

    private fun setLinkToFacebook() {
        binding.imageViewMainFacebook.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(credentials.facebook)))
        }
    }

    private fun setLinkToLinkedIn() {
        binding.imageViewMainLinkedIn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(credentials.linkedin)))
        }
    }

    private fun setLinkToInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(credentials.instagram)))
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

            showToast(requireContext(), toastText)
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
