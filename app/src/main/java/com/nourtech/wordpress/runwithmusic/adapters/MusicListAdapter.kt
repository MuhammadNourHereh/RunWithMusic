package com.nourtech.wordpress.runwithmusic.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.dialogs.Dialogs
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.services.components.MediaPlayerX
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicListAdapter(
        private var list: List<Song>,
        private val vm: MusicViewModel,
        private val fragment: Fragment
) : RecyclerView.Adapter<MusicListAdapter.MusicViewHolder>() {

    private lateinit var context: Context
    private var playlist = Playlist("unnamed")
    private val allList = list

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.tv_Title)
        val textViewArtist: TextView = itemView.findViewById(R.id.tv_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_music, parent, false)
        context = parent.context

        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.textViewTitle.text = list[position].title
        holder.textViewArtist.text = list[position].artist

        holder.itemView.setOnClickListener {
            findNavController(fragment).navigate(R.id.action_musicFragment_to_playerFragment)
        }
        holder.itemView.setOnLongClickListener {
            playlist.add(list[position])
            showMenu(it, list[position])
            true
        }

    }

    override fun getItemCount(): Int = list.size

    fun switchToPlayList() {
        list = playlist.getAll()
        notifyDataSetChanged()
    }

    fun switchToAllList() {
        list = allList
        notifyDataSetChanged()
    }


    private fun showMenu(v: View, song: Song) {
        val popup = PopupMenu(context, v)
        GlobalScope.launch(Dispatchers.IO) {
            val l = vm.getPlaylists()
            withContext(Dispatchers.Main) {
                var i = 0
                for (pl in l) {
                    popup.menu.add(0, i, Menu.NONE, "add to ${pl.getName()}")
                            .setOnMenuItemClickListener {
                                pl.add(song)
                                vm.insertPlaylist(pl)
                                true
                            }
                    i++
                }
                popup.menu.add(1, i, Menu.NONE, "create new playlist")
                        .setOnMenuItemClickListener {
                            Dialogs.newPlaylistDialog(context, vm, song).show()
                            true
                        }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    popup.menu.setGroupDividerEnabled(true)
                }

                // Show the popup menu.
                popup.show()
            }
        }


    }


}