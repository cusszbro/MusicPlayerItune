package com.luthfirr.musicplayeritune.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        //init setup view
        setupView()

        //init move to favorite
        moveToFavorite()

        // init adapter
        adapter = MusicAdapter()

        // init search event listener
        setSearchEventListener()

        // init swipe refresh event listener
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

    /**
     * set fab intent to favorite music fragment
     */
    private fun moveToFavorite() {
        binding.fabFavorite.setOnClickListener {
            startActivity(Intent(this, Class.forName("com.luthfirr.favorite.FavoriteActivity")))
        }
    }

    /**
     * Set the listener of search edit text so it will
     * call the Api when there's an update in the edit text
     * and have 1500ms debounce.
     * Also stop the current playing music when searching.
     */
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

    /**
     * Call getMusicListByArtist when swiping the layout
     */
    private fun setSwipeRefreshEventListener() {
        with(binding) {
            swipeProgress.setOnRefreshListener {
                getMusicListByArtist(etSearch.text.toString())
            }
        }
    }

    /**
     * Observe live data of music response that returned from view model
     */
    private fun getMusicListByArtist(artist: String) {
        if (checkSearchValue(artist)) {
            homeViewModel.getMusicListByArtist(artist).observe(this) {
                it?.let { music ->
                        when (music) {
                            is Resource.Loading -> {
                                setLoadingStateCondition(true)
                                Toast.makeText(this, "Wait a second", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Success -> {
                                music.data?.let { data ->
                                    setSuccessStateCondition(data)
                                }
                            }
                            is Resource.Error -> {
                                setErrorStateCondition()
                                Toast.makeText(this, "Sorry, it must be an error \\nPlease input another song", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                setEmptyStateCondition()
                                Toast.makeText(this, "Sorry, this song is an empty", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    /**
     * Check the state of search value.
     * Will call empty state condition and return false when the value is empty
     */
    private fun checkSearchValue(value: String): Boolean {
        if (value.isEmpty()) {
            setEmptyStateCondition()
            return false
        }
        return true
    }

    /**
     * Show swipe refresh loading state based on isLoading parameter
     */
    private fun setLoadingStateCondition(isLoading: Boolean) {
        with(binding) {
            swipeProgress.isRefreshing = isLoading

            if (isLoading) {
                tvError.visibility = View.GONE
                rvMusicList.visibility = View.GONE
            }
        }
    }

    /**
     * Set adapter list data and assign it into the recyclerview
     * then show to the layout.
     */
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

    /**
     * Hiding the recyclerview and show the text empty info
     */
    private fun setEmptyStateCondition() {
        setLoadingStateCondition(false)

        with(binding) {
            tvError.visibility = View.VISIBLE
            tvError.text = getString(R.string.warning_no_data)
            rvMusicList.visibility = View.GONE
        }
    }

    /**
     * Hiding the recyclerview and show the text error info
     */
    private fun setErrorStateCondition() {
        setLoadingStateCondition(false)

        with(binding) {
            tvError.visibility = View.VISIBLE
            tvError.text = getString(R.string.error_fetching_api)
            rvMusicList.visibility = View.GONE
        }
    }

}