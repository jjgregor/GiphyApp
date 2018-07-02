package com.jason.giphyapp.dagger

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(var application: Application) {

    @Provides
    @AppScope
    fun providesApplication() = application
}