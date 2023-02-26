package sdumchykov.androidApp.presentation.viewPager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
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

        val fragmentsList = listOf(MyProfileFragment(), MyContactsFragment())

        viewPagerAdapter = ViewPagerAdapter(this, fragmentsList)

        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = Tabs.MYPROFILE.ordinal

        myContactsViewModel.userLiveData.observe(viewLifecycleOwner) {
            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                when (position) {
                    Tabs.MYPROFILE.ordinal -> {
                        tab.text = getString(R.string.tab1)
                    }
                    Tabs.CONTACTS.ordinal -> {
                        tab.text = getString(R.string.tab2)

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

    enum class Tabs {
        MYPROFILE, CONTACTS
    }
}
