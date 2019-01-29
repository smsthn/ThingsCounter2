package com.smsthn.thingscounter.Fragments.containers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ThingContainerFragmentPagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment? {
        return null
    }

    override fun getCount(): Int {
        return 0
    }

}