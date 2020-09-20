package com.dirror.music.util

import android.os.Handler
import android.os.Looper
import com.dirror.music.MyApplication
import okhttp3.*
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.TimeUnit


// 单例
object MagicHttp {

    interface MagicHttpInterface {
        fun get(url: String, callBack: MagicCallback)
        fun newGet(url: String, success: (String) -> Unit, failure: (String) -> Unit) // 新的 get 请求接口，使用 Lambda
    }

    /**
     * 回调接口
     */
    interface MagicCallback {
        fun success(response: String)
        fun failure(throwable: Throwable)
    }

    // 运行在主线程，更新 UI
    fun runOnMainThread(runnable: Runnable) {
        Handler(Looper.getMainLooper()).post(runnable)
    }

    /**
     * 可用多个，不是单例类
     * 继承 MagicHttpInterface，拥有 get 方法
     */
    class OkHttpManager: MagicHttpInterface {

        override fun get(url: String, callBack: MagicCallback) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) //设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS) //设置读取超时时间
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val string = response.body?.string()!!
                        callBack.success(string)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        callBack.failure(e)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // lambda 表达式版，简化代码，增加了 cookie
        // private val cookieStore: HashMap<String, List<Cookie>> = HashMap()
        override fun newGet(url: String, success: (String) -> Unit, failure: (String) -> Unit) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(3, TimeUnit.SECONDS)
                    .cookieJar(object : CookieJar {
                        override fun loadForRequest(url: HttpUrl): List<Cookie> {
                            val cookies = MyApplication.cookieStore[url.host]
                            return cookies ?: ArrayList()
                        }

                        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                            MyApplication.cookieStore[url.host] = cookies
                        }

                    })
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val string = response.body?.string()!!
                        success.invoke(string)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        toast("MagicHttp 错误")
                        failure.invoke(e.message.toString())
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                failure.invoke(e.message.toString())
            }
        }
    }




}



