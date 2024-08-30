package com.dumchykov.socialnetworkdemo.ui.signup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.data.validateEmail
import com.dumchykov.socialnetworkdemo.data.validatePassword
import com.dumchykov.socialnetworkdemo.databinding.ActivitySignUpBinding
import com.dumchykov.socialnetworkdemo.ui.myprofile.MyProfileActivity
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color as ComposeUiColor

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.factory(DataStoreProvider(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            viewModel.navFlag.collect { flag ->
                if (flag.not()) return@collect
                val (email, password) = viewModel.credentials.value
                if (email.isEmpty() || password.isEmpty()) return@collect
                binding.textInputEmailEditText.setText(email)
                binding.textInputPasswordEditText.setText(password)
                startMyProfileActivity()
            }
        }
        binding.buttonGoogle.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ButtonGoogle()
            }
        }
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = systemBars.left + resources.getDimensionPixelSize(R.dimen._16dp),
                top = systemBars.top + resources.getDimensionPixelSize(R.dimen._16dp),
                right = systemBars.right + resources.getDimensionPixelSize(R.dimen._16dp)
            )
            insets
        }

        setEmailPasswordInputValidations()
        setRegisterClickListener()
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
            saveCredentials()
        }
        startMyProfileActivity()
    }

    private fun startMyProfileActivity() {
        val startMyProfileIntent = Intent(this, MyProfileActivity::class.java)
        startActivity(startMyProfileIntent)
        finish()
    }

    private fun saveCredentials() {
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