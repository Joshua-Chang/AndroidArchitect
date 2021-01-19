package org.devio.hi.library.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.devio.hi.library.app.data.Banner
import org.devio.hi.library.app.demo.HiLogDemoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val builder = Retrofit.Builder()
            .baseUrl("https://gank.io/api/v2/")

        base(builder)
        advance(builder)
        coroutine(builder)
    }

    private fun coroutine(builder: Retrofit.Builder) {
        val ApiService:Api = builder
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory()
            .build()
            .create(Api::class.java)

//        ApiService
//            .banner3()
    }

    private fun base(builder: Retrofit.Builder) {
        builder.build().create(Api::class.java).banner()
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    response.body().let {
                        val br = BufferedReader(it?.charStream())
                        do {
                            println(br.readLine())
                        } while (br.readLine() != null)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun advance(builder: Retrofit.Builder) {
        builder
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory()
            .build()
            .create(Api::class.java)
            .banner2()
            .enqueue(object : Callback<Banner> {
                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    Log.e("XXX", (response.body() as Banner).data[1].title)
                }

                override fun onFailure(call: Call<Banner>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_hilog -> {
                startActivity(Intent(this, HiLogDemoActivity::class.java))
            }
        }
    }
}