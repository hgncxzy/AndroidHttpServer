package com.xzy.androidhttpserver

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 *
 * @author xzy
 * @date 2020/06/18
 */
class HttpService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        HttpServer.instance?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        HttpServer.instance?.stop()
    }
}