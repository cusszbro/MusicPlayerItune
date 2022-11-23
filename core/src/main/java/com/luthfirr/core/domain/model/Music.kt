package com.luthfirr.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Music(
    val artistId: Int?,

    val artworkUrl100: String?,

    val trackName: String?,

    val artistName: String?,

    val collectionName: String?,

    val country: String?,

    val primaryGenreName: String?,

    val releaseDate: String?,

    val previewUrl: String?,

    var isFavorite: Boolean
) : Parcelable
