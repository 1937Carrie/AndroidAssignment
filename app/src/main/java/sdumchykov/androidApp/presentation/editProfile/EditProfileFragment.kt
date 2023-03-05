package sdumchykov.androidApp.presentation.editProfile

import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentEditProfileBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val parentViewModel by lazy { pagerFragment.contactsViewModel }

    override fun setListeners() {
        arrowSetListener()
        buttonSaveSetListener()
    }

    private fun arrowSetListener() {
        binding.imageEditProfileArrowBack.setOnClickListener {
            val action =
                EditProfileFragmentDirections.actionEditProfileFragmentToViewPagerFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun buttonSaveSetListener() {
        with(binding) {
            buttonEditProfileSave.setOnClickListener {
                val name = textInputLayoutEditProfileUsername.editText?.text.toString()
                val phone = textInputLayoutEditProfileEmail.editText?.text.toString()
                val address = textInputLayoutEditProfileAddress.editText?.text.toString()
                val career = textInputLayoutEditProfileCareer.editText?.text.toString()

                parentViewModel.apiEditProfile(name, phone, address, career)

                val action =
                    EditProfileFragmentDirections.actionEditProfileFragmentToViewPagerFragment()
                Navigation.findNavController(binding.root).navigate(action)


            }
        }
    }
}

