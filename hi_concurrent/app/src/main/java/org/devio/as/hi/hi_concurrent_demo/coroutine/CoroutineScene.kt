package org.devio.`as`.hi.hi_concurrent_demo.coroutine

import android.util.Log
import kotlinx.coroutines.*

object CoroutineScene {
    private val TAG: String = "CoroutineScene"
    fun startScene1() {
        GlobalScope.launch(Dispatchers.Main) {
            Log.e(TAG, "coroutine is running")
            val result1 = request1()
            val result2 = request2(result1)
            val result3 = request3(result2)
            updateUI(result3)
        }
        Log.e(TAG, "coroutine has launched")
    }
    fun startScene2(){
        GlobalScope.launch(Dispatchers.Main){
            Log.e(TAG, "coroutine is running")
            val result1 = request1()
            val deferred2 = GlobalScope.async { request2(result1) }
            val deferred3 = GlobalScope.async { request3(result1) }
            updateUI(deferred2.await(),deferred3.await())
        }
        Log.e(TAG, "coroutine has launched")
    }
    private fun updateUI(result2: String, result3: String) {
        Log.e(TAG, "updateui work on ${Thread.currentThread().name}")
        Log.e(TAG, "paramter:" + result3 + "---" + result2)
    }

    private fun updateUI(result3: String) {
        Log.e(TAG, "updateui work on ${Thread.currentThread().name}")
        Log.e(TAG, "paramter:" + result3)
    }

    suspend fun request1(): String {
        //不会暂停线程,但会暂停当前所在的协程
        delay(2 * 1000)
        //Thread.sleep(2000)  让线程休眠
        Log.e(TAG, "request1 work on ${Thread.currentThread().name}")
        return "result from request1"
    }

    suspend fun request2(result1: String): String {
        delay(2 * 1000)
        Log.e(TAG, "request2 work on ${Thread.currentThread().name}")
        return "result from request2"
    }

    suspend fun request3(result2: String): String {
        delay(2 * 1000)
        Log.e(TAG, "request3 work on ${Thread.currentThread().name}")
        return "result from request3"
    }
}