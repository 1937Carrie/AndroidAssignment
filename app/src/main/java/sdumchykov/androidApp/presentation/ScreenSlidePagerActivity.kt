package sdumchykov.androidApp.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import sdumchykov.androidApp.databinding.FragmentViewPagerBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.contacts.MyContactsFragment
import sdumchykov.androidApp.presentation.contacts.MyContactsViewModel
import sdumchykov.androidApp.presentation.myProfile.MyProfileFragment

class ScreenSlidePagerActivity :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    private val myContactsViewModel: MyContactsViewModel by activityViewModels()

    lateinit var viewPager: ViewPager2

    private lateinit var contactCollectionAdapter: ScreenSlidePagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.pager

        val fragsList = listOf(MyProfileFragment(), MyContactsFragment())

        contactCollectionAdapter = ScreenSlidePagerAdapter(this, fragsList)

        viewPager.adapter = contactCollectionAdapter
        viewPager.currentItem = 0 //TODO номер екрану MyProfile, але виконано через зад

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "My profile"
                }
                1 -> {
                    tab.text = "My contacts"

                    val badge = tab.orCreateBadge
                    badge.number = myContactsViewModel.userLiveData.value?.size ?: 0
                }
            }
        }.attach()

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