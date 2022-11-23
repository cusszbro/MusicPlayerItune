package com.luthfirr.core.utils

import com.luthfirr.core.data.source.local.entity.MusicEntity
import com.luthfirr.core.data.source.remote.response.MusicResult
import com.luthfirr.core.domain.model.Music

object DataMapper {
    fun mapResponsesToEntities(input: List<MusicResult>?): List<MusicEntity> {
        val musicList = ArrayList<MusicEntity>()
        input!!.map {
            val tourism = MusicEntity(
                artistId = it.artistId,
                artworkUrl100 = it.artworkUrl100,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                country = it.country,
                primaryGenreName = it.primaryGenreName,
                releaseDate = it.releaseDate,
                previewUrl = it.previewUrl,
                isFavorite = false
            )
            musicList.add(tourism)
        }
        return musicList
    }

    fun mapEntitiesToDomain(input: List<MusicEntity>): List<Music> =
        input.map {
            Music(
                artistId = it.artistId,
                artworkUrl100 = it.artworkUrl100,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                country = it.country,
                primaryGenreName = it.primaryGenreName,
                releaseDate = it.releaseDate,
                previewUrl = it.previewUrl,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(input: Music) = MusicEntity(
        artistId = input.artistId!!,
        artworkUrl100 = input.artworkUrl100,
        trackName = input.trackName,
        artistName = input.artistName,
        collectionName = input.collectionName,
        country = input.country,
        primaryGenreName = input.primaryGenreName,
        releaseDate = input.releaseDate,
        previewUrl = input.previewUrl,
        isFavorite = input.isFavorite
    )
}