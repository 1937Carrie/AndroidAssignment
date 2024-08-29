package com.dumchykov.socialnetworkdemo.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.validateEmail
import com.dumchykov.socialnetworkdemo.data.validatePassword
import com.dumchykov.socialnetworkdemo.databinding.ActivitySignUpBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
//        if (binding.checkboxRememberMe.isChecked) TODO("add datastore caching")
        val startMyProfileIntent = Intent(this, MainActivity::class.java)
        val email = binding.textInputEmailEditText.text.toString()
        startMyProfileIntent.putExtras(bundleOf("email" to email))
        startActivity(startMyProfileIntent)
        finish()
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