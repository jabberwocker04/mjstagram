package com.jkh.mjstagram

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.text.FieldPosition

class PageAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        if(position==0){
            return HomeFragment()
        }
        else if(position==1){
            return searchFragment()
        }
        else if(position==2){
            return addFragment()
        }
        else if(position==3){
            return starFragment()
        }
        else{
            return profileFragment()
        }
    }

}