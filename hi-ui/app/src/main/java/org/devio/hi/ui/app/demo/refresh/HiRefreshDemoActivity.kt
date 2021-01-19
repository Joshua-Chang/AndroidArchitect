package org.devio.hi.ui.app.demo.refresh

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.devio.hi.library.log.HiLog
import org.devio.hi.ui.app.MApplication
import org.devio.hi.ui.app.R
import org.devio.hi.ui.refresh.HiRefresh
import org.devio.hi.ui.refresh.HiRefreshLayout
import org.devio.hi.ui.refresh.HiTextOverView

class HiRefreshDemoActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh)
        val refreshLayout = findViewById<HiRefreshLayout>(R.id.refresh_layout)
        val hiTextOverView = HiTextOverView(this)
        refreshLayout.setRefreshOverView(hiTextOverView)
        refreshLayout.setRefreshListener(object :HiRefresh.OnRefreshListener{
            override fun onRefresh() {
                Handler().postDelayed(
                    { refreshLayout.refreshFinished() },
                    1000
                )
            }

            override fun enableRefresh()=true

        })
        initRecyclerView()
        MApplication.shortCut("031:refresh",this)
    }

    var myDataset =
        arrayOf(
            "HiRefresh",
            "HiRefresh",
            "HiRefresh",
            "HiRefresh",
            "HiRefresh",
            "HiRefresh",
            "HiRefresh"
        )

    private fun initRecyclerView() {
        recyclerView = findViewById<RecyclerView>(R.id.recycleview)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        MyAdapter(myDataset).let { recyclerView?.adapter = it }
    }

    class MyAdapter(val dataSet: Array<String>) : RecyclerView.Adapter<MyAdapter.Holder>() {

        class Holder(v: View) : RecyclerView.ViewHolder(v) {
            var textView: TextView

            init {
                textView = v.findViewById(R.id.tv_title)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            return Holder(v)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.textView.text = dataSet[position]
            holder.itemView.setOnClickListener { HiLog.d("position:$position") }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }

}