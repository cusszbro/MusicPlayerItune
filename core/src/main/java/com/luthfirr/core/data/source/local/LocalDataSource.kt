package com.luthfirr.core.data.source.local

import com.luthfirr.core.data.source.local.entity.MusicEntity
import com.luthfirr.core.data.source.local.room.MusicDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val musicDao: MusicDao) {

    fun getAllMusic(artist: String): Flow<List<MusicEntity>> = musicDao.getAllMusic(artist)

    fun getFavoriteMusic(): Flow<List<MusicEntity>> = musicDao.getFavoriteMusic()

    suspend fun insertMusic(musicList: List<MusicEntity>) = musicDao.insertMusic(musicList)

    fun setFavoriteMusic(music: MusicEntity, newState: Boolean) {
        music.isFavorite = newState
        musicDao.updateFavoriteMusic(music)
    }
}