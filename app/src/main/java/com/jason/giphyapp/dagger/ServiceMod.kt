package com.jason.giphyapp.dagger

import com.jason.giphyapp.services.GiphyService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class ServiceMod {

    @Provides
    @AppScope
    fun provideGiphyService(@Named("Giphy") restAdapter: Retrofit) : GiphyService {
        return restAdapter.create(GiphyService::class.java)
    }

}