package com.dumchykov.socialnetworkdemo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = systemBars.left + resources.getDimensionPixelSize(R.dimen._16dp),
                top = systemBars.top + resources.getDimensionPixelSize(R.dimen._16dp),
                right = systemBars.right + resources.getDimensionPixelSize(R.dimen._16dp)
            )
            insets
        }
    }
}