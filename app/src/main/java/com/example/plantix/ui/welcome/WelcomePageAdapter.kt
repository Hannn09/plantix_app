package com.example.plantix.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.plantix.ui.welcome.first.FirstWelcomeFragment
import com.example.plantix.ui.welcome.second.SecondWelcomeFragment
import com.example.plantix.ui.welcome.third.ThirdWelcomeFragment

class WelcomePageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = FirstWelcomeFragment()
            1 -> fragment = SecondWelcomeFragment()
            2 -> fragment = ThirdWelcomeFragment()
        }

        return fragment as Fragment
    }

}