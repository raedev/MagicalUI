package com.raedev.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.magical.ui.widget.bottomsheet.MagicalBottomSheetLayout
import com.magical.ui.widget.bottomsheet.behavior.RefreshNestedRecycleSheetBehavior
import com.magical.ui.widget.bottomsheet.behavior.ScrollViewSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var contentLayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentLayout = findViewById(R.id.contentLayout)
        findViewById<Button>(R.id.btn_basic).setOnClickListener { basicDemo() }
        findViewById<Button>(R.id.btn_rv).setOnClickListener { rvDemo() }
        findViewById<Button>(R.id.btn_refresh_rv).setOnClickListener { refreshNestedRvDemo() }
        refreshNestedRvDemo()
//        basicDemo()
    }


    private fun basicDemo() {
        contentLayout.removeAllViews()
        View.inflate(this, R.layout.bottom_sheet_demo_basic, contentLayout)
        contentLayout.findViewById<MagicalBottomSheetLayout>(R.id.mgl_bottom_sheet).also {
            it.behavior = ScrollViewSheetBehavior(it.context)
        }
    }

    private fun rvDemo() {
        contentLayout.removeAllViews()
        View.inflate(this, R.layout.bottom_sheet_demo_rv, contentLayout)
        contentLayout.findViewById<MagicalBottomSheetLayout>(R.id.mgl_bottom_sheet).also {
            // 启用隐藏功能
            it.behavior.isHideable = true

            findViewById<View>(R.id.tv_bg).setOnClickListener { v ->
                it.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        val recyclerView = contentLayout.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DemoAdapter().also {
            for (i in 0 until 40) {
                it.dataList.add("RV演示标题 #$i")
            }
        }
    }

    /** 刷新布局嵌套Rv */
    private fun refreshNestedRvDemo() {
        contentLayout.removeAllViews()
        View.inflate(this, R.layout.bottom_sheet_demo_rl_rv, contentLayout)

        val recyclerView = contentLayout.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DemoAdapter().also {
            for (i in 0 until 40) {
                it.dataList.add("下拉刷新嵌套RV演示 #$i")
            }
        }
        val refreshLayout =
            contentLayout.findViewById<SwipeRefreshLayout>(R.id.refresh_layout).apply {
                this.setOnRefreshListener {
                    postDelayed({
                        this.isRefreshing = false
                    }, 800)
                }
            }
        contentLayout.findViewById<MagicalBottomSheetLayout>(R.id.mgl_bottom_sheet).also {
            it.behavior = RefreshNestedRecycleSheetBehavior(it.context, refreshLayout, recyclerView)
        }
    }

    // region adapter

    class DemoAdapter : RecyclerView.Adapter<DemoHolder>() {

        val dataList = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_demo, parent, false)
            return DemoHolder(view)
        }

        override fun onBindViewHolder(holder: DemoHolder, position: Int) {
            holder.textView.text = dataList[position]
        }

        override fun getItemCount(): Int = dataList.size

    }

    class DemoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_message)

        init {
            itemView.setOnClickListener {
                Toast.makeText(
                    it.context,
                    "点击了：${(itemView.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // endregion
}