package com.luthfirr.core.domain.usecase

import com.luthfirr.core.domain.model.Music
import com.luthfirr.core.domain.repository.IMusicRepository

class MusicInteractor(private val musicRepository: IMusicRepository): MusicUseCase {

    override fun getAllMusic(artist: String) = musicRepository.getAllMusic(artist)

    override fun getFavoriteMusic() = musicRepository.getFavoriteMusic()

    override fun setFavoriteMusic(music: Music, state: Boolean) = musicRepository.setFavoriteMusic(music, state)
}