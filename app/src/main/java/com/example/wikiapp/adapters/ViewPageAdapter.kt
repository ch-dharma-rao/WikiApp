package com.example.wikiapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.wikiapp.view.ArticlesFragment
import com.example.wikiapp.view.CategoriesFragment
import com.example.wikiapp.view.PicturesFragment

class ViewPageAdapter(fm: FragmentManager, private val numofTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return numofTabs
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return ArticlesFragment()
            }
            1 -> {
                return PicturesFragment()
            }
            2 -> {
                return CategoriesFragment()
            }
            else -> return ArticlesFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) title = "Articles" else if (position == 1) title =
            "Pictures" else if (position == 2) title = "Categories"
        return title
    }


}