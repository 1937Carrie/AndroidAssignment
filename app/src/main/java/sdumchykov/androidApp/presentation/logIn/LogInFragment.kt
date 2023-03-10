package sdumchykov.androidApp.presentation.logIn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentLogInBinding
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.local.User
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.CredentialsViewModel
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.visible

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val credentialsViewModel: CredentialsViewModel by activityViewModels()
    private var credentials: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCredentials()

        handleRememberMer()
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

    private fun initCredentials() {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        val userDao = db.userDao()
        credentials = userDao.getUser()
    }

    private fun handleRememberMer() {
        with(binding) {
            if (checkBoxLogInRememberMe.isChecked && credentials != null) {
                textInputLayoutLogInEmail.editText?.setText(credentials?.email)
                textInputLayoutLogInPassword.editText?.setText(credentialsViewModel.getPassword())
                authorizeAccount()
            }
        }

    }

    private fun saveRememberMe() {
        with(binding) {
            if (checkBoxLogInRememberMe.isChecked) {
                credentialsViewModel.savePassword(textInputLayoutLogInPassword.editText?.text.toString())
            }
        }
    }

    override fun setListeners() {
        buttonLoginSetListener()
        textViewLogInSignInSetListener()
    }

    private fun buttonLoginSetListener() {
        with(binding) {
            buttonLogInLogin.setOnClickListener {
                saveRememberMe()
                authorizeAccount()
            }
        }
    }

    private fun FragmentLogInBinding.authorizeAccount() {
        val email = textInputLayoutLogInEmail.editText?.text.toString()
        val password = textInputLayoutLogInPassword.editText?.text.toString()

        credentialsViewModel.authorizeUser(email, password)
    }

    private fun textViewLogInSignInSetListener() {
        binding.textViewLogInSignIn.setOnClickListener {

            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

}