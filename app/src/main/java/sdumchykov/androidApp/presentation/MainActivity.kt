package sdumchykov.androidApp.presentation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.ActivityMainBinding
import sdumchykov.androidApp.presentation.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val findFragmentById = supportFragmentManager.findFragmentById(R.id.mainActivityNavHostFragment) // це типу Fragment?
//        val mainActivityNavHostFragment = binding.mainActivityNavHostFragment // це типу FragmentContainerView
//        TODO чому треба через supportFragmentManager виконувати наступну стрічку, а через binding.mainActivityNavHostFragment не можна (пише "я ніколи не приведу тип")?
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainActivityNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
