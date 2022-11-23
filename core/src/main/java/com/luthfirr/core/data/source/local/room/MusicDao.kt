package com.luthfirr.core.data.source.local.room

import androidx.room.*
import com.luthfirr.core.data.source.local.entity.MusicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query("SELECT * FROM music where trackName like :artist || '%'")
    fun getAllMusic(artist: String): Flow<List<MusicEntity>>

    @Query("SELECT * FROM music where isFavorite = 1")
    fun getFavoriteMusic(): Flow<List<MusicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(tourism: List<MusicEntity>)

    @Update
    fun updateFavoriteMusic(music: MusicEntity)
}