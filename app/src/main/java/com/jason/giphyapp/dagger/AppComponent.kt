package com.jason.giphyapp.dagger

import com.jason.giphyapp.activities.MainActivity
import com.jason.giphyapp.models.MainViewModel
import dagger.Component


@AppScope
@Component(modules = [(AppModule::class), (NetworkModule::class), (ServiceMod::class)])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(searchViewModel: MainViewModel)

}