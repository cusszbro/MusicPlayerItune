package com.luthfirr.core.data

import android.util.Log
import com.luthfirr.core.data.source.remote.network.ApiResponse
import com.luthfirr.core.data.source.remote.response.MusicResult
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(Resource.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map {
                        Resource.Success(it)
                    })
                }
                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map {
                        Resource.Success(it)
                    })
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(
                        Resource.Error<ResultType>(apiResponse.errorMessage)
                    )
                }
                else -> {
                    Log.e("Network bound resource", "nothing")
                }
            }
        } else {
            emitAll(loadFromDB().map {
                Resource.Success(it)
            })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<List<MusicResult>?>>

    protected abstract suspend fun saveCallResult(data: List<MusicResult>?)

    fun asFlow(): Flow<Resource<ResultType>> = result
}