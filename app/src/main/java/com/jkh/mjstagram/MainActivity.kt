package com.jkh.mjstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    lateinit var viewpager: ViewPager
    lateinit var tab:TabLayout
    lateinit var pageAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager=findViewById(R.id.viewpager)
        tab=findViewById(R.id.tab)

        pageAdapter=PageAdapter(supportFragmentManager)
        viewpager.adapter=pageAdapter
        tab.setupWithViewPager(viewpager)


        tab.getTabAt(0)?.setIcon(R.drawable.baseline_home_black_48)
        tab.getTabAt(1)?.setIcon(R.drawable.baseline_search_black_48)
        tab.getTabAt(2)?.setIcon(R.drawable.baseline_add_black_48)
        tab.getTabAt(3)?.setIcon(R.drawable.baseline_star_outline_black_48)
        tab.getTabAt(4)?.setIcon(R.drawable.baseline_person_outline_black_48)
    }
    fun moveTab(index:Int){
        tab.selectTab(tab.getTabAt(index))
        }
    }
