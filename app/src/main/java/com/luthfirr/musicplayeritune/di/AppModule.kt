package com.luthfirr.musicplayeritune.di

import com.luthfirr.core.domain.usecase.MusicInteractor
import com.luthfirr.core.domain.usecase.MusicUseCase
import com.luthfirr.musicplayeritune.detail.DetailViewModel
import com.luthfirr.musicplayeritune.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MusicUseCase> { MusicInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}