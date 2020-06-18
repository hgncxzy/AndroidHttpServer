package com.xzy.androidhttpserver

import android.util.Log
import com.koushikdutta.async.http.Multimap
import com.koushikdutta.async.http.body.AsyncHttpRequestBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.koushikdutta.async.http.server.HttpServerRequestCallback
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @author xzy
 * @date 2020/06/18
 */
class HttpServer : HttpServerRequestCallback {
    var httpServer = AsyncHttpServer()
    fun start() {
        Log.d(TAG, "Starting http server...")
        httpServer["[\\d\\D]*", this]
        httpServer.post("[\\d\\D]*", this)
        httpServer.listen(PORT_DEFAULT)
    }

    fun stop() {
        Log.d(TAG, "Stopping http server...")
        httpServer.stop()
    }

    private fun sendResponse(response: AsyncHttpServerResponse, json: JSONObject) {
        // Enable CORS
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.send(json)
    }

    override fun onRequest(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse
    ) {
        val uri = request.path
        Log.d(TAG, "onRequest $uri")
        val params: Any?
        params = if (request.method == "GET") {
            request.query
        } else if (request.method == "POST") {
            val contentType = request.headers["Content-Type"]
            if (contentType == "application/json") {
                (request.body as AsyncHttpRequestBody<*>).get()
            } else {
                (request.body as AsyncHttpRequestBody<*>).get()
            }
        } else {
            Log.d(TAG, "Unsupported Method")
            return
        }
        if (params != null) {
            Log.d(TAG, "params = $params")
        }
        when (uri) {
            "/devices" -> handleDevicesRequest(params, response)
            else -> handleInvalidRequest(params, response)
        }
    }

    private fun handleDevicesRequest(
        params: Any?,
        response: AsyncHttpServerResponse
    ) {
        // Print request params
        val id: String
        when (params) {
            is Multimap -> {
                id = params.getString("id")
                Log.d(TAG, "[Multimap] id=$id")
            }
            is JSONObject -> {
                id = try {
                    Log.d(TAG, params.toString())
                    params.getString("id")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return
                }
                Log.d(TAG, "[JSONObject] id=$id")
            }
            else -> {
                Log.e(TAG, "Invalid request params")
                return
            }
        }

        // Send JSON format response
        try {
            val item1 = JSONObject()
            item1.put("name", "D1")
            item1.put("id", "123")
            val item2 = JSONObject()
            item2.put("name", "D2")
            item2.put("id", "456")
            val array = JSONArray()
            array.put(item1)
            array.put(item2)
            val json = JSONObject()
            json.put("devices", array.toString())
            sendResponse(response, json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun handleInvalidRequest(
        params: Any?,
        response: AsyncHttpServerResponse
    ) {
        Log.d("params", params.toString())
        val json = JSONObject()
        try {
            json.put("error", "Invalid API")
            sendResponse(response, json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "HttpServer"
        private var mInstance: HttpServer? = null
        var PORT_DEFAULT = 7302
        val instance: HttpServer?
            get() {
                if (mInstance == null) {
                    synchronized(HttpServer::class.java) {
                        if (mInstance == null) {
                            mInstance = HttpServer()
                        }
                    }
                }
                return mInstance
            }
    }
}