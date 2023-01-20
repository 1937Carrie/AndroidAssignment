package sdumchykov.task3

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import sdumchykov.task3.databinding.FragmentSignUpBinding

private const val MINIMUM_PASSWORD_LENGTH = 8
private const val PATTERN_DIGIT = "\\d"
private const val PATTERN_CHARACTERS = "[a-zA-Z]+"
private const val EMAIL = "Email"
private const val PASSWORD = "Password"
private const val REQUEST_KEY = "requestKey"
private const val DATA = "data"

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonRegisterDisable(buttonRegister)

            textInputDoOnTextChanged()
            buttonRegisterSetOnClickListener(buttonRegister, textInputEmail, textInputPassword)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun buttonRegisterSetOnClickListener(
        buttonRegister: Button,
        textInputEmail: AppCompatEditText,
        textInputPassword: AppCompatEditText
    ) {
        buttonRegister.setOnClickListener {
            if (binding.checkBoxRememberMe.isChecked) {
                val cachedData = this.requireActivity().getPreferences(MODE_PRIVATE)
                val editor = cachedData.edit()

                editor.putString(EMAIL, textInputEmail.text.toString())
                editor.putString(PASSWORD, textInputPassword.text.toString())

                editor.apply()

                val toast = Toast.makeText(
                    context, "${cachedData.getString(EMAIL, "Not found")}\n" + "${
                        cachedData.getString(PASSWORD, "Not found")
                    }", Toast.LENGTH_LONG
                )
                toast.show()
            }

            val result = textInputEmail.text.toString()
            setFragmentResult(REQUEST_KEY, bundleOf(DATA to result))

            if (binding.checkBoxRememberMe.isChecked) {
                //TODO не повертатись назад, якщо поставлена галочка
            }

            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, MyProfileFragment())
                addToBackStack("")
            }
        }
    }

    private fun textInputDoOnTextChanged() {
        with(binding) {
            textInputEmail.doOnTextChanged { _, _, _, _ ->
                textInputLayoutEmail.error =
                    if (!Patterns.EMAIL_ADDRESS.matcher(textInputEmail.text.toString()).matches()) {
                        resources.getString(R.string.error_message_email)
                    } else {
                        null
                    }

                val emailError = textInputLayoutEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutPassword.error.isNullOrEmpty()

                buttonRegister.isEnabled = emailError && passwordError
            }
            textInputPassword.doOnTextChanged { _, _, _, _ ->
                val lessThanEightSymbols =
                    textInputPassword.text.toString().length < MINIMUM_PASSWORD_LENGTH
                val notContainsDigits =
                    !textInputPassword.text.toString().contains(Regex(PATTERN_DIGIT))
                val notContainsCharacters =
                    !textInputPassword.text.toString().contains(Regex(PATTERN_CHARACTERS))

                textInputLayoutPassword.error =
                    if (lessThanEightSymbols || notContainsDigits || notContainsCharacters) {
                        resources.getString(R.string.error_message_password)
                    } else {
                        null
                    }

                val emailError = textInputLayoutEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutPassword.error.isNullOrEmpty()

                buttonRegister.isEnabled = emailError && passwordError
            }
        }
    }

    private fun buttonRegisterDisable(buttonRegister: Button) {
        buttonRegister.isEnabled = false
    }

}