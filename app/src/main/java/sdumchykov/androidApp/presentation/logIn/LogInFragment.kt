package sdumchykov.androidApp.presentation.logIn

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentLogInBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.CredentialsViewModel

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {
    private val credentialsViewModel: CredentialsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTestData()
    }

    private fun setTestData() {
        with(binding) {
            textInputLayoutLogInEmail.editText?.setText("")
            textInputLayoutLogInPassword.editText?.setText("")
        }
    }

    override fun setListeners() {
        buttonLoginSetListener()
        textViewLogInSignInSetListener()
    }

    private fun buttonLoginSetListener() {
        with(binding) {
            buttonLogInLogin.setOnClickListener {
                val email = textInputLayoutLogInEmail.editText?.text.toString()
                val password = textInputLayoutLogInPassword.editText?.text.toString()

                progressBarLogIn.isVisible = true
                credentialsViewModel.authorizeUser(binding, email, password)
                progressBarLogIn.isVisible = false

                val action = LogInFragmentDirections.actionLogInFragmentToMainActivity()
                Navigation.findNavController(binding.root).navigate(action)
                activity?.finish()
            }
        }
    }

    private fun textViewLogInSignInSetListener() {
        binding.textViewLogInSignIn.setOnClickListener {

            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

}