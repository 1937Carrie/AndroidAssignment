package com.dumchykov.socialnetworkdemo.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.validateEmail
import com.dumchykov.socialnetworkdemo.data.validatePassword
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentLoginBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.AuthenticationResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeCachedCredentials()
        super.onViewCreated(view, savedInstanceState)
        setLoginClickListener()
        setSignUpClickListener()
        setForgotPasswordClickListener()
        observeLoginAttempt()
        setEmailPasswordInputValidations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLoginAttempt() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    if (binding.checkboxRememberMe.isChecked) saveCredentials().join()
                    binding.layoutProgress.root.visibility = View.GONE
                    val (currentUser, accessToken, refreshToken) = (state as ResponseState.Success<*>).data as AuthenticationResponse
                    sharedViewModel.updateState {
                        copy(
                            currentUser = currentUser,
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_pagerFragment)
                }
            }
        }
    }

    private fun setEmailPasswordInputValidations() {
        binding.textInputEmailEditText.doOnTextChanged { text, _, _, _ ->
            updateEmailInputError(text.toString())
        }
        binding.textInputPasswordEditText.doOnTextChanged { text, _, _, _ ->
            updatePasswordInputError(text.toString())
        }
    }

    private fun updatePasswordInputError(password: String) {
        val passwordValidationResult = validatePassword(password)
        binding.textInputPasswordLayout.error =
            if (passwordValidationResult) null else getString(R.string.text_input_password_error_description)
    }

    private fun updateEmailInputError(email: String) {
        val emailValidationResult = validateEmail(email)
        binding.textInputEmailLayout.error =
            if (emailValidationResult) null else getString(R.string.text_input_email_description)
    }

    private fun setForgotPasswordClickListener() {
        binding.textForgotPassword.setOnClickListener {
            val url =
                "https://memi.klev.club/uploads/posts/2024-05/memi-klev-club-qtvt-p-memi-chelovek-sidit-za-stolom-s-butilkoi-7.jpg"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun saveCredentials(): Job {
        val email = binding.textInputEmailEditText.text.toString()
        val password = binding.textInputPasswordEditText.text.toString()
        return viewModel.saveCredentials(email, password)
    }

    private fun setLoginClickListener() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.textInputEmailEditText.text.toString()
            val password = binding.textInputPasswordEditText.text.toString()
            val emailValidationResult = validateEmail(email)
            val passwordValidationResult = validatePassword(password)
            when (emailValidationResult && passwordValidationResult) {
                true -> viewModel.authorize(email, password)
                false -> {
                    updateEmailInputError(binding.textInputEmailEditText.text.toString())
                    updatePasswordInputError(binding.textInputPasswordEditText.text.toString())
                }
            }
        }
    }

    private fun setSignUpClickListener() {
        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun observeCachedCredentials() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.credentialsState.collect { (email, password) ->
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    binding.textInputEmailEditText.setText(email)
                    binding.textInputPasswordEditText.setText(password)
                    viewModel.authorize(email, password)
                }
            }
        }
    }
}