package com.dumchykov.socialnetworkdemo.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun setEmailPasswordInputValidations() {
        binding.textInputEmailEditText.doOnTextChanged { text, _, _, _ ->
            val emailValidationResult = validateEmail(text.toString())
            binding.textInputEmailLayout.error = when (emailValidationResult) {
                true -> {
                    Log.d(
                        "AAA",
                        binding.textInputEmailLayout.defaultHintTextColor?.defaultColor.toString()
                    )
                    null
                }

                false -> {
                    Log.d(
                        "AAA",
                        binding.textInputEmailLayout.defaultHintTextColor?.defaultColor.toString()
                    )
                    "Incorrect E-Mail address"
                }
            }
        }
        binding.textInputPasswordEditText.doOnTextChanged { text, _, _, _ ->
            val passwordValidationResult = validatePassword(text.toString())
            binding.textInputPasswordLayout.error = when (passwordValidationResult) {
                true -> null
                false -> "Your password must include a minimum of 6 characters."
            }
        }
    }
}