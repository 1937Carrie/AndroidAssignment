package com.dumchykov.socialnetworkdemo.ui.screens.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import com.dumchykov.socialnetworkdemo.databinding.FragmentSignUpBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.AuthenticationResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonGoogle.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ButtonGoogle()
            }
        }
        setEmailPasswordInputValidations()
        setRegisterClickListener()
        setSignInClickListener()
        observeRegisterAttempt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeRegisterAttempt() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    if (binding.checkboxRememberMe.isChecked) saveCredentialsAndNavigate().join()
                    binding.layoutProgress.root.visibility = View.GONE
                    val (_, accessToken, refreshToken) = (state as ResponseState.Success<*>).data as AuthenticationResponse
                    sharedViewModel.updateState {
                        copy(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }
                    findNavController().navigate(R.id.action_signUpFragment_to_signUpExtendedFragment)
                }
            }
        }
    }

    private fun setSignInClickListener() {
        binding.textSignIn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setRegisterClickListener() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.textInputEmailEditText.text.toString()
            val password = binding.textInputPasswordEditText.text.toString()
            val emailValidationResult = validateEmail(email)
            val passwordValidationResult = validatePassword(password)
            when (emailValidationResult && passwordValidationResult) {
                true -> registerAccount()
                false -> {
                    updateEmailInputError(binding.textInputEmailEditText.text.toString())
                    updatePasswordInputError(binding.textInputPasswordEditText.text.toString())
                }
            }
        }
    }

    private fun registerAccount() {
        val email = binding.textInputEmailEditText.text.toString()
        val password = binding.textInputPasswordEditText.text.toString()
        viewModel.register(email, password)
    }

    private fun saveCredentialsAndNavigate(): Job {
        val email = binding.textInputEmailEditText.text.toString()
        val password = binding.textInputPasswordEditText.text.toString()
        return viewModel.saveCredentials(email, password)
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
}