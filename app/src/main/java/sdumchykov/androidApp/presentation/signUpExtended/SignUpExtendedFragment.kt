package sdumchykov.androidApp.presentation.signUpExtended

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import sdumchykov.androidApp.databinding.FragmentSignUpExtendedBinding
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.CredentialsViewModel

class SignUpExtendedFragment :
    BaseFragment<FragmentSignUpExtendedBinding>(FragmentSignUpExtendedBinding::inflate) {
    private val viewModel: CredentialsViewModel by viewModels()

    override fun setListeners() {
        buttonForwardSetListener()
        textFieldPhoneSetListener()
    }

    override fun setObservers() {
        setStatusObserver()
    }

    private fun setStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    val action =
                        SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToMainActivity()
                    findNavController(binding.root).navigate(action)
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }

    }

    private fun buttonForwardSetListener() {
        with(binding) {
            buttonSignUpExtendedForward.setOnClickListener {
                val name = textInputLayoutSignUpExtendedUserName.editText?.text.toString()
                val phone = textInputLayoutSignUpExtendedMobilePhone.editText?.text.toString()

                viewModel.apiEditProfile(name, phone)
            }
        }
    }

    private fun textFieldPhoneSetListener() {
        binding.textInputEditTextSignUpExtendedMobilePhone.addTextChangedListener(
            PhoneNumberFormattingTextWatcher()
        )
    }
}