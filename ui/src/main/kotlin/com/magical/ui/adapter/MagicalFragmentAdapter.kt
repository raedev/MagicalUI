@file:Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")

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
open class MagicalFragmentAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val TITLE: String = "FRAGMENT_TITLE"
        const val ICON: String = "FRAGMENT_ICON"
        const val POSITION: String = "FRAGMENT_POSITION"

        fun getTitle(fragment: Fragment): String? {
            return fragment.arguments?.getString(TITLE)
        }

        fun getIcon(fragment: Fragment): Int? {
            return fragment.arguments?.getInt(ICON)
        }

        fun getPosition(fragment: Fragment): Int {
            return fragment.arguments?.getInt(POSITION) ?: -1
        }
    }

    private val fragments = mutableListOf<Fragment>()

    fun addFragment(title: String, iconId: Int, fragment: Fragment) {
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
        val bundle = fragment.requireArguments()
        bundle.putString(TITLE, title)
        bundle.putInt(ICON, iconId)
        fragments.add(fragment)
        bundle.putInt(POSITION, fragments.indexOf(fragment))
    }

    fun addFragment(title: String, fragment: Fragment) = addFragment(title, -1, fragment)

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getItem(position).requireArguments().getString(TITLE)
    }

    protected open fun getIcon(position: Int): Int {
        return getItem(position).requireArguments().getInt(ICON)
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