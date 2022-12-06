@file:Suppress("DEPRECATION")

package com.magical.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.magical.ui.widget.MagicalTextView

/**
 * FragmentAdapter 管理Fragment的添加
 * @author RAE
 * @date 2022/08/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
open class ViewPagerFragmentAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = mutableListOf<Fragment>()

    fun addFragment(title: String, iconId: Int, fragment: Fragment) {
        val bundle = fragment.arguments ?: Bundle()
        bundle.putString("fragment_title", title)
        bundle.putInt("fragment_icon", iconId)
        fragment.arguments = bundle
        fragments.add(fragment)
    }

    fun addFragment(title: String, fragment: Fragment) = addFragment(title, -1, fragment)

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getItem(position).requireArguments().getString("fragment_title")
    }

    protected open fun getIcon(position: Int): Int {
        return getItem(position).requireArguments().getInt("fragment_icon")
    }

    /**
     * 重新创建TAB
     */
    fun recreate(tabLayout: TabLayout, @LayoutRes layoutId: Int) {
        tabLayout.removeAllTabs()
        for (i in 0 until count) {
            addTab(tabLayout, layoutId, getPageTitle(i).toString(), getIcon(i))
        }
    }

    protected open fun addTab(
        tabLayout: TabLayout,
        @LayoutRes layoutId: Int,
        title: String,
        @DrawableRes iconId: Int
    ) {
        val tab = tabLayout.newTab()
        tab.customView = View.inflate(tabLayout.context, layoutId, null)
        val titleView = tab.customView!!.findViewById<MagicalTextView>(android.R.id.title)
        titleView.text = title
        titleView.drawableTop = iconId
        tabLayout.addTab(tab)
    }
}