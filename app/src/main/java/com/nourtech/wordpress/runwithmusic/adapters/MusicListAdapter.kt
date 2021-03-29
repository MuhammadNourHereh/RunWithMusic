package com.nourtech.wordpress.runwithmusic.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.others.Constants.ACTION_START_MUSIC
import com.nourtech.wordpress.runwithmusic.others.Constants.CURRENT_SONG_PATH
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.TrackingService

class MusicListAdapter(private val list: List<Song>):
        RecyclerView.Adapter<MusicListAdapter.MusicViewHolder>() {

    private lateinit var context: Context

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewTitle : TextView = itemView.findViewById(R.id.tv_Title)
        val textViewArtist : TextView = itemView.findViewById(R.id.tv_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_music, parent, false)
        context = parent.context

        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.textViewTitle.text =  list[position].title
        holder.textViewArtist.text =  list[position].artist

        holder.textViewTitle.setOnClickListener {
            val path = list[position].path
            sendCommandToService(ACTION_START_MUSIC, path)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun sendCommandToService(action: String, path: String) =
            Intent(context, TrackingService::class.java).also {
                it.action = action
                it.putExtra(CURRENT_SONG_PATH, path)
                context.startService(it)
            }
}