package sdumchykov.androidApp.presentation.viewPager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentViewPagerBinding
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.viewPager.contacts.MyContactsFragment
import sdumchykov.androidApp.presentation.viewPager.contacts.MyContactsViewModel
import sdumchykov.androidApp.presentation.viewPager.myProfile.MyProfileFragment

@AndroidEntryPoint
class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var viewPager: ViewPager2
    val myContactsViewModel: MyContactsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.pager

        val fragsList = listOf(MyProfileFragment(), MyContactsFragment())

        viewPagerAdapter = ViewPagerAdapter(this, fragsList)

        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 0 // TODO номер екрану MyProfile, але виконано через зад

        myContactsViewModel.userLiveData.observe(viewLifecycleOwner) {
            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "My profile"
                    }
                    1 -> {
                        tab.text = "My contacts"

                        val badge = tab.orCreateBadge
                        badge.number = it.size
                    }
                }
            }.attach()
        }
    }

    private inner class ViewPagerAdapter(
        fragment: Fragment,
        private val fragsListHere: List<Fragment>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = fragsListHere.size

        override fun createFragment(position: Int): Fragment {
            return fragsListHere[position]
        }
    }
}
