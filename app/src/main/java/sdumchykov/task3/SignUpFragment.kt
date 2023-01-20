package sdumchykov.task3

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import sdumchykov.task3.databinding.FragmentSignUpBinding

private const val EMAIL = "Email"
private const val PASSWORD = "Password"
private const val REQUEST_KEY = "requestKey"
private const val DATA = "data"

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            with(binding) {
                val lessThanEightSymbols = textInputPassword.text?.length!! < 8
                val notContainsDigits = !textInputPassword.text?.contains(Regex("\\d"))!!
                val notContainsCharacters = !textInputPassword.text?.contains(Regex("[a-zA-Z]+"))!!

                if (!textInputEmail.text?.contains(Regex(".+@.+\\..+"))!!) {
                    textInputLayoutEmail.error = resources.getString(R.string.error_message_email)
                } else {
                    textInputLayoutEmail.error = null
                }

                if (lessThanEightSymbols || notContainsDigits || notContainsCharacters) {
                    textInputLayoutPassword.error =
                        resources.getString(R.string.error_message_password)
                } else {
                    textInputLayoutPassword.error = null
                }


                val emailError = textInputLayoutEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutPassword.error.isNullOrEmpty()

                buttonRegister.isEnabled = emailError && passwordError
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonRegisterDisable(buttonRegister)

            textInputAddTextChangedListener(textInputEmail)
            textInputAddTextChangedListener(textInputPassword)

            buttonRegisterSetOnClickListener(buttonRegister, textInputEmail, textInputPassword)

//            startMainActivity()

            if (savedInstanceState != null) {
                textInputEmail.setText(savedInstanceState.getString("email"))
                textInputPassword.setText(savedInstanceState.getString("password"))
            }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun startMainActivity() {
        val cachedData = this.requireActivity().getPreferences(MODE_PRIVATE)

//        if (cachedData.getString("Email", "")?.length!! > 0) {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//            intent.putExtra("email", cachedData.getString("Email", ""))
//
//            startActivity(intent)
//            finish()
//        }
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

    private fun textInputAddTextChangedListener(textInputEmail: AppCompatEditText) {
        textInputEmail.addTextChangedListener(watcher)
    }

    private fun buttonRegisterDisable(buttonRegister: Button) {
        buttonRegister.isEnabled = false
    }

}