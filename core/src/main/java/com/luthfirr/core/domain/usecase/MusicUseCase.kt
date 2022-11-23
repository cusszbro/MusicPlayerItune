package com.luthfirr.core.domain.usecase

import com.luthfirr.core.data.Resource
import com.luthfirr.core.domain.model.Music
import kotlinx.coroutines.flow.Flow

interface MusicUseCase {
    fun getAllMusic(artist: String): Flow<Resource<List<Music>>>
    fun getFavoriteMusic(): Flow<List<Music>>
    fun setFavoriteMusic(music: Music, state: Boolean)
}