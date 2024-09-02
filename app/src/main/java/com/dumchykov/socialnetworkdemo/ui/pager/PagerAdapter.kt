package com.dumchykov.socialnetworkdemo.ui.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dumchykov.socialnetworkdemo.ui.mycontacts.MyContactsFragment
import com.dumchykov.socialnetworkdemo.ui.myprofile.MyProfileFragment

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Page.entries.size

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int).
        val fragment = when (position) {
            Page.MyProfile.ordinal -> {
                MyProfileFragment()
            }

            else -> {
                MyContactsFragment()
            }
        }
        return fragment
    }
}