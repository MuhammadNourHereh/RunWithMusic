package com.nourtech.wordpress.runwithmusic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nourtech.wordpress.runwithmusic.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.runwithmusic.others.Song

class PlaylistAdapter(
        private var list: List<Song>
) : RecyclerView.Adapter<PlaylistAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.tv_Title)
        val textViewArtist: TextView = itemView.findViewById(R.id.tv_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_music, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.textViewTitle.text = list[position].title
        holder.textViewArtist.text = list[position].artist
    }

    override fun getItemCount(): Int = list.size


}