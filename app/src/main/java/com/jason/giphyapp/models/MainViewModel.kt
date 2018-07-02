package com.jason.giphyapp.models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.jason.giphyapp.GiphyApp
import com.jason.giphyapp.services.GiphyService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(app: Application) : AndroidViewModel(app) {

    init {
        (app as GiphyApp).getAppComponent().inject(this)
    }

    @Inject
    lateinit var service: GiphyService

    var searchQuery: String = ""
    val gifObserver = MutableLiveData<ArrayList<Gif>>()
    var currentPosition = ZERO
    private var offset = ZERO
    private var pages = ZERO
    private var totalPageCount = ZERO
    private var isDownloadingNextPage = false

    fun getGifsFromSearch() {
        service.getDankGifs(searchQuery, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        gifObserver.value = it.data
                        offset += GIF_OFFSET
                        totalPageCount = it.pagination.total_count
                        pages++
                        isDownloadingNextPage = false
                    } ?: Log.e(TAG, "Error getting gifs $it")
                }, { t: Throwable? -> Log.e(TAG, "Error getting gifs $t") })
    }

    fun getTrendingGifs() {
        service.getTrendingGifs(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        gifObserver.value = it.data
                        offset += GIF_OFFSET
                        totalPageCount = it.pagination.total_count
                        pages++
                        isDownloadingNextPage = false
                    } ?: Log.e(TAG, "Error getting gifs $it")
                }, { t: Throwable? -> Log.e(TAG, "Error getting gifs $t") })
    }

    fun resetPaging() {
        offset = ZERO
        totalPageCount = ZERO
        pages = ZERO
        currentPosition = ZERO
        isDownloadingNextPage = false
    }

    fun shouldDownLoadNextPage(totalCount: Int, firstVisible: Int, visibleCount: Int) {
        currentPosition = firstVisible
        if (!isDownloadingNextPage && visibleCount + firstVisible >= totalCount && pages <= totalPageCount) {
            isDownloadingNextPage = true

            if (searchQuery.isNotEmpty()) {
                getGifsFromSearch()
            } else {
                getTrendingGifs()
            }
        }
    }

    companion object {
        private val TAG: String = MainViewModel::class.java.simpleName
        const val GIF_OFFSET = 25
        const val ZERO = 0
    }
}