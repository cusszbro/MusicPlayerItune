package com.luthfirr.musicplayeritune.detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.luthfirr.core.R.*
import com.luthfirr.core.domain.model.Music
import com.luthfirr.musicplayeritune.R
import com.luthfirr.musicplayeritune.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val viewModel: DetailViewModel by viewModel()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailMusic = intent.getParcelableExtra<Music>(EXTRA_DATA)

        setupView()

        showDetailMusic(detailMusic)

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showDetailMusic(detailMusic: Music?) {
        detailMusic?.let {
            Glide.with(this@DetailActivity)
                .load(detailMusic.artworkUrl100)
                .centerCrop()
                .into(binding.ivBackgroundDetail)
            Glide.with(this@DetailActivity)
                .load(detailMusic.artworkUrl100)
                .into(binding.ivAlbumArtDetail)
            binding.tvSongNameDetail.text = detailMusic.trackName
            binding.tvSongArtistDetail.text = detailMusic.artistName
            binding.tvSongAlbumDetail.text = detailMusic.collectionName
            binding.tvSongGenreDetail.text = detailMusic.primaryGenreName
            binding.tvSongReleaseDetail.text = detailMusic.releaseDate

            binding.ivPlay.setOnClickListener { playSelectedMusic(detailMusic) }

            var statusFavorite = detailMusic.isFavorite
            setStatusFavorite(statusFavorite)
            binding.ivFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                viewModel.setFavoriteMusic(detailMusic, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(this,drawable.ic_favorite_fill))
        } else {
            binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(this,drawable.ic_favorite_nofill))
        }
    }

    private fun playSelectedMusic(music: Music) {

        // init music player
        mediaPlayer = MediaPlayer()

        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        try {
            // get music from preview url
            mediaPlayer.setDataSource(music.previewUrl)

            // play music
            mediaPlayer.prepareAsync()

            // set on media player preparing is done
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()

                // set media control state
                setMediaControlState(music)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setMediaControlState(music: Music? = null) {
        with(binding) {
            if (this@DetailActivity::mediaPlayer.isInitialized) {
                if (mediaPlayer.isPlaying) {
                    ivPlay.setImageResource(R.drawable.ic_pause)
                    ivPlay.setOnClickListener {
                        pauseCurrentPlayingMusic()
                    }
                } else {
                    ivPlay.setImageResource(R.drawable.ic_play)
                    ivPlay.setOnClickListener {
                        mediaPlayer.start()
                        setMediaControlState(music)
                    }
                }
            }
        }
    }

    private fun pauseCurrentPlayingMusic() {
        if (this::mediaPlayer.isInitialized) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                setMediaControlState()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setMediaControlState()
    }

    override fun onStop() {
        super.onStop()
        pauseCurrentPlayingMusic()
        setMediaControlState()
    }

}