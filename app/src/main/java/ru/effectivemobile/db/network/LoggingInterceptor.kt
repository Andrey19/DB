package ru.effectivemobile.db.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d("InterceptorDemo", "Response code: ${response.code} for ${request.url}")
        return response
    }
}