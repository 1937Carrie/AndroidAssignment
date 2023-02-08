package sdumchykov.androidApp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.presentation.signUp.SignUpFragment

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(R.layout.activity_sign_up) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //TODO why i have to leave it?
        if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<SignUpFragment>(R.id.signUpActivityNavHostFragment)
                }

        }
    }
}