package com.nourtech.wordpress.runwithmusic.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.others.Constants
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_SET_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.CHOSEN_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Constants.SEND_CURRENT_PLAYLIST
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.TrackingService
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel

class PlaylistsAdapter(
        private var list: MutableList<Playlist>,
        private val fragment: Fragment,
        private val vm: MusicViewModel
) : RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder>() {

    private lateinit var context: Context
    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.tv_Title)
        val textViewCount: TextView = itemView.findViewById(R.id.tv_count)
        val imageViewPlay: ImageView = itemView.findViewById(R.id.iv_play)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist, parent, false)
        context = parent.context
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.textViewTitle.text = list[position].getName()
        holder.textViewCount.text = list[position].getCount().toString()
        holder.itemView.setOnClickListener {
            findNavController(fragment).navigate(R.id.action_playlistsFragment_to_playListFragment,
                    Bundle().apply { putInt(CHOSEN_PLAYLIST, position) })
        }
        holder.imageViewPlay.setOnClickListener {
            sendCommandToService(list[position])
            findNavController(fragment).navigate(R.id.action_playlistsFragment_to_playerFragment)
        }
        holder.itemView.setOnLongClickListener {
            showPopupMenu(it, list[position])
            true
        }
    }

    override fun getItemCount(): Int = list.size

    private fun showPopupMenu(v: View, playlist: Playlist) {
        val popup = PopupMenu(context, v)
        popup.menu.add(1, 0, Menu.NONE, "delete the playlist").setOnMenuItemClickListener {
            vm.deletePlaylists(playlist)
            list.remove(playlist)
            notifyDataSetChanged()
            true
        }
        popup.show()
    }

    private fun sendCommandToService(playlist: Playlist) =
            Intent(fragment.requireContext(), TrackingService::class.java).also {
                it.action = ACTION_SET_PLAYLIST
                it.putExtra(SEND_CURRENT_PLAYLIST, playlist)
                fragment.requireContext().startService(it)
            }

}