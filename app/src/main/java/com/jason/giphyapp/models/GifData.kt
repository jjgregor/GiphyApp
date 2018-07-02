package com.jason.giphyapp.models

import java.io.Serializable

data class GifData(var data: ArrayList<Gif> = arrayListOf(),
                   val pagination: Pagination = Pagination())

data class Gif(val type: String = "",
               val id: String = "",
               val slug: String = "",
               val url: String = "",
               val username: String = "",
               val source: String = "",
               val rating: String = "",
               val import_datetime: String = "",
               val images: Image = Image()) : Serializable

data class Image(val fixed_height: GifUrl = GifUrl(),
                 val original: GifUrl = GifUrl()) : Serializable

data class GifUrl(val url: String = "") : Serializable

data class Pagination(val total_count: Int = 0,
                      val count: Int = 0,
                      val offset: Int = 0)