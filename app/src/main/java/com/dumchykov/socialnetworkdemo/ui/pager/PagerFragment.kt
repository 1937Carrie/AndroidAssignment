package com.dumchykov.socialnetworkdemo.ui.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.dumchykov.socialnetworkdemo.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PagerFragment : Fragment() {
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = PagerAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = pagerAdapter
        viewPager.doOnPreDraw { startPostponedEnterTransition() }

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                Page.MyProfile.ordinal -> Page.MyProfile.name
                else -> Page.MyContacts.name
            }
        }.attach()
    }

    fun changeCurrentItem(item: Int) {
        viewPager.currentItem = item
    }
}