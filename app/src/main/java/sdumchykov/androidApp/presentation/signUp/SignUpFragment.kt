package sdumchykov.androidApp.presentation.signUp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentSignUpBinding
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.domain.utils.Constants.FEATURE_FLAG
import sdumchykov.androidApp.domain.utils.Constants.PASSWORD_KEY
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.myProfile.MyProfileFragment

private const val MINIMUM_PASSWORD_LENGTH = 8
private const val PATTERN_DIGIT = "\\d"
private const val PATTERN_CHARACTERS = "[a-zA-Z]+"

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
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
                buttonSignUpRegister,
                editTextSignUpEmail,
                editTextSignUpPassword
            )
        }
    }

    private fun letNavigateMyProfileFragment() {
        val savedEmail =
            this.requireActivity().getPreferences(MODE_PRIVATE).getString(EMAIL_KEY, "").toString()
        if (savedEmail.isNotEmpty()) {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_signUpFragment_to_myProfileFragment)
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
                    if (lessThanEightSymbols || notContainsDigits || notContainsCharacters)
                        resources.getString(R.string.error_message_password)
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
                val cachedData = this.requireActivity().getPreferences(MODE_PRIVATE)
                val editor = cachedData.edit()

                editor.putString(EMAIL_KEY, editTextSignUpEmail.text.toString())
                editor.putString(PASSWORD_KEY, editTextSignUpPassword.text.toString())

                editor.apply()

                val toast = Toast.makeText(
                    activity?.applicationContext,
                    "${cachedData.getString(EMAIL_KEY, "Not found")}\n" + "${
                        cachedData.getString(PASSWORD_KEY, "Not found")
                    }",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }

            if (FEATURE_FLAG) {
                val action =
                    SignUpFragmentDirections.actionSignUpFragmentToMyProfileFragment(
                        editTextSignUpEmail.text.toString()
                    )
                Navigation.findNavController(binding.root).navigate(action)
            } else {
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fragmentContainerView, MyProfileFragment())
                    addToBackStack("")
                }
            }
        }
    }
}