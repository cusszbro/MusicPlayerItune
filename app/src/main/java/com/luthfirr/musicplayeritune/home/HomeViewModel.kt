package com.luthfirr.musicplayeritune.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luthfirr.core.data.Resource
import com.luthfirr.core.domain.model.Music
import com.luthfirr.core.domain.usecase.MusicUseCase

class HomeViewModel(private val musicUseCase: MusicUseCase): ViewModel() {

    fun getMusicListByArtist(artist: String): LiveData<Resource<List<Music>>> {
        return musicUseCase.getAllMusic(artist).asLiveData()
    }
}