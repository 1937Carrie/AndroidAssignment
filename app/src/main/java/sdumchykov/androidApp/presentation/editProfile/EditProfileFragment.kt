package sdumchykov.androidApp.presentation.editProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentEditProfileBinding
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.utils.ext.showToast

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val viewModel: EditProfileViewModel by viewModels()

    private var pathToLoadedImageFromGallery: String = Constants.STUB_IMAGE_URI
    private var imageLoaderLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageView: AppCompatImageView =
                binding.imageViewSignUpExtendedChooseProfileImageFromGallery
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageData = result.data?.data ?: return@registerForActivityResult
                pathToLoadedImageFromGallery = imageData.toString()
                imageView.setImage(imageData)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fillInFields()
    }

    override fun setListeners() {
        arrowSetListener()
        setImageViewSetListener()
        buttonSaveSetListener()
    }

    override fun setObservers() {
        setStatusObserver()
    }

    private fun setStatusObserver() {
        viewModel.statusUserContacts.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    val action =
                        EditProfileFragmentDirections.actionEditProfileFragmentToViewPagerFragment()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                Status.ERROR -> {
                    context?.showToast("Failed to edit profile")
                }
                Status.LOADING -> {}
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            fillInFields()
        }
    }

    private fun fillInFields() {
        with(binding) {
            val user = viewModel.user.value

            val image = user?.image ?: Constants.STUB_IMAGE_URI
            imageViewEditProfilePicture.setImage(Uri.parse(image))

            textInputLayoutEditProfileUsername.editText?.setText(user?.name)
            textInputLayoutEditProfileAddress.editText?.setText(user?.address)
            textInputLayoutEditProfileCareer.editText?.setText(user?.career)
            textInputLayoutEditProfilePhone.editText?.setText(user?.phone)
            textInputLayoutEditProfileDateOfBirth.editText?.setText(user?.birthday)
        }
    }

    private fun arrowSetListener() {
        binding.imageEditProfileArrowBack.setOnClickListener {
            val action =
                EditProfileFragmentDirections.actionEditProfileFragmentToViewPagerFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun setImageViewSetListener() {
        binding.imageViewSignUpExtendedChooseProfileImageFromGallery.setOnClickListener {
            val gallery = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            imageLoaderLauncher.launch(gallery)
        }
    }

    private fun buttonSaveSetListener() {
        with(binding) {
            buttonEditProfileSave.setOnClickListener {
                val name = textInputLayoutEditProfileUsername.editText?.text.toString()
                val phone = textInputLayoutEditProfileEmail.editText?.text.toString()
                val address = textInputLayoutEditProfileAddress.editText?.text.toString()
                val career = textInputLayoutEditProfileCareer.editText?.text.toString()
                val DOB = textInputLayoutEditProfileDateOfBirth.editText?.text.toString()
//                val image = pathToLoadedImageFromGallery

                viewModel.apiEditProfile(name, phone, address, career, DOB)
            }
        }
    }

}

