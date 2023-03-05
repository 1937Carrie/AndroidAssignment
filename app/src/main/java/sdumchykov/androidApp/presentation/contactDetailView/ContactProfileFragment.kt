package sdumchykov.androidApp.presentation.contactDetailView

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentContactProfileBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.setImageCacheless

@AndroidEntryPoint
class ContactProfileFragment :
    BaseFragment<FragmentContactProfileBinding>(FragmentContactProfileBinding::inflate) {

    private val args: ContactProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileData()
    }

    override fun setListeners() {
        buttonArrowSetListener()
    }

    private fun setProfileData() {
        with(binding) {
            val user = args.user

            textViewName.text = user.name
            textViewProfession.text = user.career
            textViewAddress.text = user.address
            textViewContactDetailViewNumber.text = user.phone

            imageViewPicture.setImageCacheless(user.image)
        }
    }

    private fun buttonArrowSetListener() {
        with(binding) {
            imageButtonArrowBack.setOnClickListener {
//                val action =
//                    ContactProfileFragmentDirections
//                        .actionContactProfileFragmentToScreenSlidePagerActivity()
//                Navigation.findNavController(root).navigate(action)

                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
