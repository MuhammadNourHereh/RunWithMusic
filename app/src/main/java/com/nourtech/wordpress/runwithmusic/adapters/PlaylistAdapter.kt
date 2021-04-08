package com.nourtech.wordpress.runwithmusic.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nourtech.wordpress.runwithmusic.R
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.others.Constants.SEND_CURRENT_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.TrackingService

class PlaylistAdapter(
        private var list: List<Song>,
        private var fragment: Fragment
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

        holder.itemView.setOnClickListener {

            findNavController(fragment).navigate(R.id.action_playListFragment_to_playerFragment)
        }

    }

    override fun getItemCount(): Int = list.size

    private fun sendCommandToService(action: String, playlist: Playlist) =
            Intent(fragment.requireContext(), TrackingService::class.java).also {
                it.action = action
                it.putExtra(SEND_CURRENT_PLAYLIST, playlist)
                fragment.requireContext().startService(it)
            }
}