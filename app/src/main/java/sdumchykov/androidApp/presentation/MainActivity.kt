package sdumchykov.androidApp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.domain.utils.Constants.FEATURE_FLAG
import sdumchykov.androidApp.presentation.signUp.SignUpFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (!FEATURE_FLAG) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<SignUpFragment>(R.id.fragmentContainerView)
                }
            }
        }
    }
}