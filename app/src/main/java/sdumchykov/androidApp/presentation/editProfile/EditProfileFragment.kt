package sdumchykov.androidApp.presentation.editProfile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentEditProfileBinding
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.viewPager.contacts.ContactsViewModel

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    private val contactsViewModel: ContactsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateFields()
    }

    override fun setListeners() {
        arrowSetListener()
        buttonSaveSetListener()
    }

    private fun inflateFields() {
        with(binding) {
            val db = Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java, "database-name"
            ).allowMainThreadQueries().build()
            val userDao = db.userDao()
            val user = userDao.getUser()

            textInputLayoutEditProfileUsername.editText?.setText(user.name)
            textInputLayoutEditProfileAddress.editText?.setText(user.address)
            textInputLayoutEditProfileCareer.editText?.setText(user.career)
            textInputLayoutEditProfilePhone.editText?.setText(user.phone)
            textInputLayoutEditProfileDateOfBirth.editText?.setText(user.birthday)

        }
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
                val DOB = textInputLayoutEditProfileDateOfBirth.editText?.text.toString()

                contactsViewModel.apiEditProfile(name, phone, address, career, DOB)

                val action =
                    EditProfileFragmentDirections.actionEditProfileFragmentToViewPagerFragment()
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }
}

