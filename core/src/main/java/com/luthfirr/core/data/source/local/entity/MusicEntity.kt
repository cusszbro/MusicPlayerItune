package com.luthfirr.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class MusicEntity(
    @PrimaryKey
    @ColumnInfo(name = "artistId")
    val artistId: Int?,

    @ColumnInfo(name = "artwokUrl100")
    val artworkUrl100: String?,

    @ColumnInfo(name = "trackName")
    val trackName: String?,

    @ColumnInfo(name = "artistName")
    val artistName: String?,

    @ColumnInfo(name = "collectionName")
    val collectionName: String?,

    @ColumnInfo(name = "country")
    val country: String?,

    @ColumnInfo(name = "primaryGenreName")
    val primaryGenreName: String?,

    @ColumnInfo(name = "releaseDate")
    val releaseDate: String?,

    @ColumnInfo(name = "previewUrl")
    val previewUrl: String?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
