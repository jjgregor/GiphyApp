package com.jason.giphyapp

import android.app.Application
import com.jason.giphyapp.dagger.AppComponent
import com.jason.giphyapp.dagger.AppModule
import com.jason.giphyapp.dagger.DaggerAppComponent
import com.jason.giphyapp.dagger.NetworkModule

class GiphyApp : Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(applicationContext))
                .build()
    }

    fun getAppComponent() = component

}