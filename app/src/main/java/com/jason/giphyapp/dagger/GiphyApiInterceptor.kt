package com.jason.giphyapp.dagger

import android.content.Context
import com.jason.giphyapp.R
import okhttp3.Interceptor
import okhttp3.Response

class GiphyApiInterceptor (private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val urlBuilder = request.url()
                .newBuilder()
                .addQueryParameter(API_KEY, context.getString(R.string.api_key))
        request = request.newBuilder().url(urlBuilder.build()).build()
        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY = "api_key"
    }
}