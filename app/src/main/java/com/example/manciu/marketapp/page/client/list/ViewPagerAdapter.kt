package com.example.manciu.marketapp.page.client.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.manciu.marketapp.page.client.list.available.ClientAvailableListFragment
import com.example.manciu.marketapp.page.client.list.bought.ClientBoughtListFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val TAB_BOUGHT = "BOUGHT"
        const val TAB_AVAILABLE = "AVAILABLE"
    }

    override fun getItem(position: Int): Fragment =
            if (position == 1) ClientBoughtListFragment() else ClientAvailableListFragment()

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? =
            if (position == 1) TAB_BOUGHT else TAB_AVAILABLE

}