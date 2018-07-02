package com.jason.giphyapp.services

import com.jason.giphyapp.models.GifData
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface GiphyService {

    @GET("/v1/gifs/search")
    fun getDankGifs(@Query("q") q: String,
                    @Query("offset") offset: Int): Observable<GifData>

    @GET("/v1/gifs/trending")
    fun getTrendingGifs(@Query("offset") offset: Int): Observable<GifData>

}