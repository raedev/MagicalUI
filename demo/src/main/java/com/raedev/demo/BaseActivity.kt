package com.raedev.demo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * @author RAE
 * @date 2023/02/19
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected open var homeAsUpEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (homeAsUpEnabled) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (homeAsUpEnabled && item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }


}