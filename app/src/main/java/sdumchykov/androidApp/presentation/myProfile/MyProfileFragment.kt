package sdumchykov.androidApp.presentation.myProfile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()
    private val args: MyProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
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
            activity?.viewModelStore?.clear()

            val fetchContactList = !viewModel.getFetchContactList()
            viewModel.setFetchContactList(fetchContactList)

            val toastText = if (fetchContactList) SHOW_CONTACT_LIST
            else SHOW_HARDCODED_LIST
            Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_myProfileFragment_to_myContactsFragment)
        }
    }

}