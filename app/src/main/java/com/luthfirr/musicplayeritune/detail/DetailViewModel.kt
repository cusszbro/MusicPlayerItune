package com.luthfirr.musicplayeritune.detail

import androidx.lifecycle.ViewModel
import com.luthfirr.core.domain.model.Music
import com.luthfirr.core.domain.usecase.MusicUseCase

class DetailViewModel(private val musicUseCase: MusicUseCase): ViewModel() {

    fun setFavoriteMusic(music: Music, newStatus:Boolean) =
        musicUseCase.setFavoriteMusic(music, newStatus)
}