package sdumchykov.androidApp.presentation.signUp

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
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

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            letNavigateMyProfileFragment()
            buttonRegisterDisable(buttonSignUpRegister)
            textInputDoOnTextChanged()
        }
    }

    override fun setListeners() {
        super.setListeners()

        with(binding) {
            buttonRegisterSetOnClickListener(
                buttonSignUpRegister, editTextSignUpEmail, editTextSignUpPassword
            )
        }
    }

    private fun letNavigateMyProfileFragment() {
        val savedEmail = signUpViewModel.getEmail()

        if (savedEmail.isNotEmpty()) {
            findNavController(binding.root).navigate(
                SignUpFragmentDirections.actionSignUpFragmentToMainActivity(
                    savedEmail
                )
            )
            activity?.finish()
        }
    }

    private fun buttonRegisterDisable(buttonSignUpRegister: Button) {
        buttonSignUpRegister.isEnabled = false
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

    private fun buttonRegisterSetOnClickListener(
        buttonSignUpRegister: Button,
        editTextSignUpEmail: AppCompatEditText,
        editTextSignUpPassword: AppCompatEditText
    ) {
        buttonSignUpRegister.setOnClickListener {
            if (binding.checkBoxSignUpRememberMe.isChecked) {

                signUpViewModel.saveEmail(editTextSignUpEmail.text.toString())
                signUpViewModel.savePassword(editTextSignUpPassword.text.toString())

                val toast = Toast.makeText(
                    activity?.applicationContext,
                    "${signUpViewModel.getEmail()}\n" + signUpViewModel.getPassword(),
                    Toast.LENGTH_LONG
                )
                toast.show()
            } else {
                signUpViewModel.saveEmail(editTextSignUpEmail.text.toString())
            }

            val action = SignUpFragmentDirections.actionSignUpFragmentToMainActivity(
                editTextSignUpEmail.text.toString()
            )
            findNavController(binding.root).navigate(action)
            activity?.finish()
        }
    }
}
