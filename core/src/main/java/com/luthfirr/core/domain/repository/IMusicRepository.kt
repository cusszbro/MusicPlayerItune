package com.luthfirr.core.domain.repository

import com.luthfirr.core.data.Resource
import com.luthfirr.core.domain.model.Music
import kotlinx.coroutines.flow.Flow

interface IMusicRepository {

    fun getAllMusic(artist: String): Flow<Resource<List<Music>>>

    fun getFavoriteMusic(): Flow<List<Music>>

    fun setFavoriteMusic(music: Music, state: Boolean)

}