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
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SET_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.EXTRA_CURRENT_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.services.TrackingService

class PlaylistAdapter(
        private var list: Playlist,
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
        holder.textViewTitle.text = list.get(position).title
        holder.textViewArtist.text = list.get(position).artist

        holder.itemView.setOnClickListener {
            list.setIndex(position)
            sendCommandToService()
            findNavController(fragment).navigate(R.id.action_playListFragment_to_playerFragment)
        }

    }

    override fun getItemCount(): Int = list.getCount()

    private fun sendCommandToService() =
            Intent(fragment.requireContext(), TrackingService::class.java).also {
                it.action = ACTION_SET_PLAYLIST
                it.putExtra(EXTRA_CURRENT_PLAYLIST, list)
                fragment.requireContext().startService(it)
            }
}