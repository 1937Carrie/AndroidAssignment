package sdumchykov.androidApp.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import sdumchykov.androidApp.databinding.FragmentViewPagerBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.contacts.MyContactsFragment
import sdumchykov.androidApp.presentation.myProfile.MyProfileFragment

class ScreenSlidePagerActivity : BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    lateinit var viewPager: ViewPager2

    private lateinit var contactCollectionAdapter: ScreenSlidePagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.pager

        val fragsList = listOf(MyProfileFragment(), MyContactsFragment())

        contactCollectionAdapter = ScreenSlidePagerAdapter(this, fragsList)

        viewPager.adapter = contactCollectionAdapter
        viewPager.currentItem = 0 //TODO номер екрану MyProfile, але виконано через зад
    }

    private inner class ScreenSlidePagerAdapter(
        fragment: Fragment,
        private val fragsListHere: List<Fragment>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = fragsListHere.size

        override fun createFragment(position: Int): Fragment {
            return fragsListHere[position]
        }
    }
}