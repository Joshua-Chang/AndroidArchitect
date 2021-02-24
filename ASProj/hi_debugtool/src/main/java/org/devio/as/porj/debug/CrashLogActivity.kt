package org.devio.`as`.porj.debug

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_crash_log.*
import kotlinx.android.synthetic.main.activity_crash_log_list_item.view.*
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.crash.CrashMgr
import java.io.File


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/18 3:29 PM
 * @创建人：常守达
 * @备注：
 */

class CrashLogActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_log)
        val crashFiles = CrashMgr.crashFiles()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = CrashLogAdapter(crashFiles)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.shape_hi_debug_tool_divider
            )!!
        )
        recycler_view.addItemDecoration(decoration)
    }

    inner class CrashLogAdapter(val crashFiles: Array<File>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                layoutInflater.inflate(
                    R.layout.activity_crash_log_list_item,
                    parent,
                    false
                )
            ) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val file = crashFiles.get(position)
            holder.itemView.file_title.text=file.name
            holder.itemView.file_title.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra("subject", "")
                intent.putExtra("body", "")
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        this@CrashLogActivity,
                        "${packageName}.fileProvider",
                        file
                    )
                } else {
                    Uri.fromFile(file)
                }
                //分享文件流
                intent.putExtra(Intent.EXTRA_STREAM, uri)//添加文件
                if (file.name.endsWith(".txt")) {
                    intent.type = "text/plain"//纯文本
                } else {
                    intent.type = "application/octet-stream" //二进制文件流
                }
                startActivity(Intent.createChooser(intent, "分享Crash 日志文件"))
            }
        }

        override fun getItemCount(): Int {
            return crashFiles.size
        }

    }
}