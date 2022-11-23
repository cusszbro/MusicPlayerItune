package com.luthfirr.musicplayeritune.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.luthfirr.core.data.Resource
import com.luthfirr.core.domain.model.Music
import com.luthfirr.core.ui.MusicAdapter
import com.luthfirr.core.utils.textChanges
import com.luthfirr.musicplayeritune.R
import com.luthfirr.musicplayeritune.databinding.ActivityHomeBinding
import com.luthfirr.musicplayeritune.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        setupLocal()

        moveToFavorite()

        adapter = MusicAdapter()

        setSearchEventListener()

        setSwipeRefreshEventListener()
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

    private fun setupLocal() {
        binding.ibSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun moveToFavorite() {
        binding.fabFavorite.setOnClickListener {
            startActivity(Intent(this, Class.forName("com.luthfirr.favorite.FavoriteActivity")))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun setSearchEventListener() {
        binding.etSearch.textChanges()
            .filterNot {
                it.isNullOrBlank()
            }
            .debounce(1500)
            .distinctUntilChanged { old, new ->
                old != new
            }
            .onEach {
                // set adapter selected position into -1
                adapter.selectedItemPosition = MusicAdapter.NOT_SET_YET

                getMusicListByArtist(it.toString())
            }
            .launchIn(lifecycleScope)
    }

    private fun setSwipeRefreshEventListener() {
        with(binding) {
            swipeProgress.setOnRefreshListener {
                getMusicListByArtist(etSearch.text.toString())
            }
        }
    }

    private fun getMusicListByArtist(artist: String) {
        if (checkSearchValue(artist)) {
            homeViewModel.getMusicListByArtist(artist).observe(this) {
                it?.let { music ->
                        when (music) {
                            is Resource.Loading -> {
                                setLoadingStateCondition(true)
                            }
                            is Resource.Success -> {
                                music.data?.let { data ->
                                    setSuccessStateCondition(data)
                                }
                            }
                            is Resource.Error -> {
                                setErrorStateCondition()
                                Toast.makeText(this, getString(R.string.error_toast), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                setEmptyStateCondition()
                                Toast.makeText(this, getString(R.string.empty_toast), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun checkSearchValue(value: String): Boolean {
        if (value.isEmpty()) {
            setEmptyStateCondition()
            return false
        }
        return true
    }

    private fun setLoadingStateCondition(isLoading: Boolean) {
        with(binding) {
            swipeProgress.isRefreshing = isLoading

            if (isLoading) {
                tvError.visibility = View.GONE
                rvMusicList.visibility = View.GONE
            }
        }
    }

    private fun setSuccessStateCondition(data: List<Music>) {
        setLoadingStateCondition(false)

        with(binding) {
            adapter.setData(data)
            adapter.onItemClick = { selectedMusic ->
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, selectedMusic)
                startActivity(intent)
            }

            rvMusicList.adapter = adapter

            // set the visibility
            tvError.visibility = View.GONE
            rvMusicList.visibility = View.VISIBLE
        }
    }

    private fun setEmptyStateCondition() {
        setLoadingStateCondition(false)

        with(binding) {
            tvError.visibility = View.VISIBLE
            tvError.text = getString(R.string.warning_no_data)
            rvMusicList.visibility = View.GONE
        }
    }

    private fun setErrorStateCondition() {
        setLoadingStateCondition(false)

        with(binding) {
            tvError.visibility = View.VISIBLE
            tvError.text = getString(R.string.error_fetching_api)
            rvMusicList.visibility = View.GONE
        }
    }

}