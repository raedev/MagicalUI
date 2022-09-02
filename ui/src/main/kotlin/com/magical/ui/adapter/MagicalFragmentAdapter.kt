package com.magical.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * FragmentAdapter 管理Fragment的添加
 * @author RAE
 * @date 2022/08/10
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
class MagicalFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

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

    fun getIcon(position: Int): Int {
        return getItem(position).requireArguments().getInt("fragment_icon")
    }
}