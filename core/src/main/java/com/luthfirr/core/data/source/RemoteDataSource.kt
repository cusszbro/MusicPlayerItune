package com.luthfirr.core.data.source

import android.util.Log
import com.luthfirr.core.data.source.remote.network.ApiResponse
import com.luthfirr.core.data.source.remote.network.ApiService
import com.luthfirr.core.data.source.remote.response.MusicResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllMusic(artist: String): Flow<ApiResponse<List<MusicResult>?>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getListMusicByArtist(artist)
                val dataArray = response.results
                if (dataArray!!.isNotEmpty()){
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private const val TAG = "RemoteDataSource"
    }

}