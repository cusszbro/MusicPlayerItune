package com.luthfirr.core.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.luthfirr.core.databinding.ItemMusicBinding
import com.luthfirr.core.domain.model.Music

class MusicAdapter: RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    private var _binding: ItemMusicBinding? = null
    private val binding get() =_binding!!
    var listData = ArrayList<Music>()
    var onItemClick: ((Music) -> Unit)? = null
    var selectedItemPosition = NOT_SET_YET

    fun setData(newListData: List<Music>?) {
        if (newListData == null) return

        listData.apply {
            // clear and add all item to list
            clear()
            addAll(newListData)

            // update adapter one by one
            forEachIndexed { index, _ -> notifyItemChanged(index) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMusicBinding.bind(itemView)

        init {
            binding.cardMusic.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }

        fun bind(data: Music) {
            with(binding) {
                // set album image src
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.artworkUrl100)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivAlbumArt)

                // set text for song info
                tvSongName.text = data.trackName
                tvSongArtist.text = data.artistName
                tvSongAlbum.text = data.collectionName
                Log.e("TAG", "${data.artistName}")
                setSelectedItemState(data)
            }
        }

        private fun setSelectedItemState(data: Music) {
            if (selectedItemPosition != NOT_SET_YET) {
                val currentPosition = listData.indexOf(data)
                if (currentPosition == selectedItemPosition) {
                    // set current selected item
                    binding.ivMusicNote.visibility = View.VISIBLE
                } else {
                    // set previous selected item
                    binding.ivMusicNote.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemMusicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        const val NOT_SET_YET = -1
    }
}