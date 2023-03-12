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
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.utils.ext.showToast
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragmentDirections
import sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts.FetchContacts

private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    private val myProfileViewModel: MyProfileViewModel by viewModels()
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val parentViewModel by lazy { pagerFragment.contactsViewModel }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myProfileViewModel.getDB()
        checkPrelaunchPermissions()
    }

    override fun setListeners() {
        imageViewMainProfilePictureSetListener()
        editProfileSetListener()
        buttonViewMyContactsSetOnClickListener()
    }

    override fun setObservers() {
        setStatusObserver()
    }

    private fun setStatusObserver() {
        setResponseStatusObserver()
        setUserObserver()
    }

    private fun setResponseStatusObserver() {
        parentViewModel.statusUserContacts.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {}
                Status.ERROR -> {
                    context?.showToast("Failed to pull account contact list")
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun setUserObserver() {
        myProfileViewModel.user.observe(viewLifecycleOwner) {
            fillInProfile()
        }
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

    private fun fillInProfile() {
        setMainPicture()
        setTextToName()
        setTextToProfession()
        setTextToAddress()
        setLinksToSocialNetworks()
    }

    private fun setMainPicture() {
        val image = myProfileViewModel.user.value?.image ?: Constants.STUB_IMAGE_URI

        binding.imageViewMainProfilePicture.setImage(Uri.parse(image))
    }

    private fun setTextToName() {
        binding.textViewMainName.text = myProfileViewModel.user.value?.name
    }

    private fun setTextToProfession() {
        binding.textViewMainProfession.text = myProfileViewModel.user.value?.career
    }

    private fun setTextToAddress() {
        binding.textViewMainAddress.text = myProfileViewModel.user.value?.address
    }

    private fun setLinksToSocialNetworks() {
        setLinkToFacebook()
        setLinkToLinkedIn()
        setLinkToInstagram()
    }

    private fun setLinkToFacebook() {
        binding.imageViewMainFacebook.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(myProfileViewModel.user.value?.facebook)
                )
            )
        }
    }

    private fun setLinkToLinkedIn() {
        binding.imageViewMainLinkedIn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(myProfileViewModel.user.value?.linkedin)
                )
            )
        }
    }

    private fun setLinkToInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(myProfileViewModel.user.value?.instagram)
                )
            )
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

            context?.showToast(toastText)
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
