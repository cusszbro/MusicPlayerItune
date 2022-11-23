package com.luthfirr.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luthfirr.core.domain.usecase.MusicUseCase

class FavoriteViewModel(musicUseCase: MusicUseCase) : ViewModel() {

    val favoriteMusic = musicUseCase.getFavoriteMusic().asLiveData()

}