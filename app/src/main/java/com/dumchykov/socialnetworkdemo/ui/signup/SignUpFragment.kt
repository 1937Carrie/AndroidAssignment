package com.dumchykov.socialnetworkdemo.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.validateEmail
import com.dumchykov.socialnetworkdemo.data.validatePassword
import com.dumchykov.socialnetworkdemo.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color as ComposeUiColor

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels(
        factoryProducer = {
            SignUpViewModel.factory(DataStoreProvider(requireContext()))
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navFlag.collect { flag ->
                if (flag.not()) return@collect
                val (email, password) = viewModel.credentials.value
                binding.textInputEmailEditText.setText(email)
                binding.textInputPasswordEditText.setText(password)
                navigateToMyProfile()
            }
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                true -> doOnRegisterClick()
                false -> {
                    updateEmailInputError(binding.textInputEmailEditText.text.toString())
                    updatePasswordInputError(binding.textInputPasswordEditText.text.toString())
                }
            }
        }
    }

    private fun doOnRegisterClick() {
        if (binding.checkboxRememberMe.isChecked) {
            saveCredentialsAndNavigate()
        } else {
            navigateToMyProfile()
        }
    }

    private fun navigateToMyProfile() {
        findNavController().navigate(R.id.action_signUpFragment_to_signUpExtendedFragment)
    }

    private fun saveCredentialsAndNavigate() {
        val email = binding.textInputEmailEditText.text.toString()
        val password = binding.textInputPasswordEditText.text.toString()
        viewModel.saveCredentials(email, password)
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

@Preview(heightDp = 40)
@Composable
fun ButtonGoogle() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            ComposeUiColor.White,
            ComposeUiColor.White,
            ComposeUiColor.White,
            ComposeUiColor.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.google).uppercase(),
                color = colorResource(R.color.black),
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.opensans_bold,
                        FontWeight.W600,
                        FontStyle.Normal
                    )
                ),
                letterSpacing = 1.5.sp
            )
        }
    }
}