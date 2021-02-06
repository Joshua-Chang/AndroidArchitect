package org.devio.`as`.proj.main.biz.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.AccountApi
import org.devio.`as`.proj.main.model.CourseNotice
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.ui.item.HiAdapter

@Route(path = "/notice/list")
class NoticeListActivity : AppCompatActivity() {
    private lateinit var adapter: HiAdapter
    private lateinit var courseNotice: CourseNotice
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, enableTranslucent = false)
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_notice_list)
        action_back.setOnClickListener {
            onBackPressed()
        }
        initUI()
        queryCourseNotice()
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object :HiCallback<CourseNotice>{
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    response.data?.let { bindData(it) }
                }

                override fun onFailed(throwable: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun bindData(data: CourseNotice) {
        courseNotice=data
        data.list?.map {
            adapter.addItem(0,NoticeItem(it),true)
        }
    }

    private fun initUI() {
        val llm = LinearLayoutManager(this)
        adapter = HiAdapter(this)
        list.layoutManager = llm
        list.adapter = adapter
    }
}