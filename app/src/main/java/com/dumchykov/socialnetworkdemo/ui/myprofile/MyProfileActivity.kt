package com.dumchykov.socialnetworkdemo.ui.myprofile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider
import com.dumchykov.socialnetworkdemo.databinding.ActivityMainBinding
import com.dumchykov.socialnetworkdemo.ui.signup.SignUpActivity
import kotlinx.coroutines.launch

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyProfileViewModel by viewModels {
        MyProfileViewModel.factory(DataStoreProvider(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right
            )
            insets
        }

        setName()
        setLogOutClickListener()
    }

    private fun setName() {
        lifecycleScope.launch {
            viewModel.name.collect { name ->
                binding.textName.text = name
            }
        }
    }

    private fun setLogOutClickListener() {
        binding.textLogOut.setOnClickListener {
            viewModel.clearCredentials()
            lifecycleScope.launch {
                viewModel.exitFlag.collect {
                    if (it) {
                        startActivity(Intent(this@MyProfileActivity, SignUpActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}