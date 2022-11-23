package com.luthfirr.core.data

import com.luthfirr.core.data.source.RemoteDataSource
import com.luthfirr.core.data.source.local.LocalDataSource
import com.luthfirr.core.data.source.remote.network.ApiResponse
import com.luthfirr.core.data.source.remote.response.MusicResult
import com.luthfirr.core.domain.model.Music
import com.luthfirr.core.domain.repository.IMusicRepository
import com.luthfirr.core.utils.AppExecutors
import com.luthfirr.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMusicRepository {

    override fun getAllMusic(artist: String): Flow<Resource<List<Music>>> =
        object : NetworkBoundResource<List<Music>, List<MusicResult>>() {


            override fun shouldFetch(data: List<Music>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<MusicResult>?>> {
                return remoteDataSource.getAllMusic(artist)
            }

            override fun loadFromDB(): Flow<List<Music>> {
                return localDataSource.getAllMusic(artist).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override suspend fun saveCallResult(data: List<MusicResult>?) {
                val musicList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertMusic(musicList)
            }
        }.asFlow()

    override fun getFavoriteMusic(): Flow<List<Music>> {
        return localDataSource.getFavoriteMusic().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteMusic(music: Music, state: Boolean) {
        val musicEntity = DataMapper.mapDomainToEntity(music)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMusic(musicEntity, state) }
    }
}