package sdumchykov.androidApp.presentation.logIn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentLogInBinding
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.CredentialsViewModel
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.visible

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {
    private val credentialsViewModel: CredentialsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTestData()
    }

    override fun setObservers() {
        credentialsViewModel.status.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response.status) {
                    Status.SUCCESS -> {
                        textViewLogInAuthorizeResponseText.gone()
                        progressBarLogIn.gone()

                        val action = LogInFragmentDirections.actionLogInFragmentToMainActivity()
                        Navigation.findNavController(binding.root).navigate(action)
                        activity?.finish()
                    }
                    Status.ERROR -> {
                        progressBarLogIn.gone()
                        textViewLogInAuthorizeResponseText.visible()
                        textViewLogInAuthorizeResponseText.text =
                            getString(credentialsViewModel.status.value?.message ?: R.string.oops)
                    }
                    Status.LOADING -> {
                        textViewLogInAuthorizeResponseText.gone()
                        progressBarLogIn.visible()
                    }
                }
            }
        }
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

                credentialsViewModel.authorizeUser(email, password)
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