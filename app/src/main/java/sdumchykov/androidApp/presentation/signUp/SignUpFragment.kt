package sdumchykov.androidApp.presentation.signUp

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentSignUpBinding
import sdumchykov.androidApp.presentation.base.BaseFragment

private const val MINIMUM_PASSWORD_LENGTH = 8
private const val PATTERN_DIGIT = "\\d"
private const val PATTERN_CHARACTERS = "[a-zA-Z]+"

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val credentialsViewModel: CredentialsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegisterDisable()
        textInputDoOnTextChanged()
    }

    override fun setListeners() {
        super.setListeners()

        buttonRegisterSetListener()
        signInSetListener()
    }

    private fun buttonRegisterDisable() {
        binding.buttonSignUpRegister.isEnabled = false
    }

    private fun textInputDoOnTextChanged() {
        with(binding) {
            editTextSignUpEmail.doOnTextChanged { _, _, _, _ ->
                textInputLayoutSignUpEmail.error =
                    if (!Patterns.EMAIL_ADDRESS.matcher(editTextSignUpEmail.text.toString())
                            .matches()
                    ) resources.getString(R.string.error_message_email)
                    else null

                val emailError = textInputLayoutSignUpEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutSignUpPassword.error.isNullOrEmpty()

                buttonSignUpRegister.isEnabled = emailError && passwordError
            }

            editTextSignUpPassword.doOnTextChanged { _, _, _, _ ->
                val lessThanEightSymbols =
                    editTextSignUpPassword.text.toString().length < MINIMUM_PASSWORD_LENGTH
                val notContainsDigits =
                    !editTextSignUpPassword.text.toString().contains(Regex(PATTERN_DIGIT))
                val notContainsCharacters =
                    !editTextSignUpPassword.text.toString().contains(Regex(PATTERN_CHARACTERS))

                textInputLayoutSignUpPassword.error =
                    if (lessThanEightSymbols || notContainsDigits || notContainsCharacters) resources.getString(
                        R.string.error_message_password
                    )
                    else null

                val emailError = textInputLayoutSignUpEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutSignUpPassword.error.isNullOrEmpty()

                buttonSignUpRegister.isEnabled = emailError && passwordError
            }
        }
    }

    private fun buttonRegisterSetListener() {
        binding.buttonSignUpRegister.setOnClickListener {
            val email = binding.textInputLayoutSignUpEmail.editText?.text.toString()
            val password = binding.textInputLayoutSignUpPassword.editText?.text.toString()

            credentialsViewModel.register(email, password)

            val action = SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment()
            findNavController(binding.root).navigate(action)
        }
    }

    private fun signInSetListener() {
        binding.textViewSignUpSignIn.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
            findNavController(binding.root).navigate(action)
        }
    }
}
