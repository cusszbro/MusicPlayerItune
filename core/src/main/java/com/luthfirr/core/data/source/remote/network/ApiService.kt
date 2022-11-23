package com.luthfirr.core.data.source.remote.network

import com.luthfirr.core.data.source.remote.response.MusicResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getListMusicByArtist(
        @Query("term") artist: String
    ): MusicResponse
}