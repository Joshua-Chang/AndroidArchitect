package org.devio.hi.config.core

import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 从cdn读取配置
 */
class NetFileConnection {
    private var httpUrlConnection: HttpURLConnection? = null
    fun syncRequest(url: String): String? {
        var uri = URL(url)
        var response: String?=null
        httpUrlConnection = uri.openConnection() as HttpURLConnection
        httpUrlConnection?.apply {
            connectTimeout = 5000
            readTimeout = 5000
            useCaches = false
            doInput = true
            requestMethod = "GET"
        }
        if (httpUrlConnection?.responseCode == 200) {
            response = getResponse()
        }
        return response
    }

    private fun getResponse(): String? {
        return if (httpUrlConnection == null) {
            null
        } else {
            var inputStream: InputStream? = null
            var bos: ByteArrayOutputStream? = null
            try {
                inputStream = httpUrlConnection!!.inputStream
                bos = ByteArrayOutputStream()
                val buffer = ByteArray(2048)
                var length:Int
                while (inputStream.read(buffer).also { length=it }!=-1){
                    bos.write(buffer,0,length)
                }
                String(bos.toByteArray())
            }catch (e: IOException){
                throw e
            }
            finally {
                HiConfigUtil.close(inputStream)
                HiConfigUtil.close(bos)
            }
        }
    }

}