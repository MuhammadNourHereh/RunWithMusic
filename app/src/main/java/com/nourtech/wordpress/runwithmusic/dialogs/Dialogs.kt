package com.nourtech.wordpress.runwithmusic.dialogs

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.core.view.setPadding
import com.nourtech.wordpress.runwithmusic.R
import com.nourtech.wordpress.runwithmusic.others.Playlist
import com.nourtech.wordpress.runwithmusic.others.Song
import com.nourtech.wordpress.runwithmusic.ui.viewmodels.MusicViewModel

object Dialogs {

     fun newPlaylistDialog(context: Context, vm: MusicViewModel, song: Song): AlertDialog {
        val view = EditText(context).apply {
            setPadding(8)
        }
        val title = context.resources.getString(R.string.create_a_new_playlist)
        val save = context.resources.getString(R.string.create)
        val cancel = context.resources.getString(R.string.cancel)

        return AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(save) { _, _ ->
                    vm.insertPlaylist(Playlist(view.text.toString(), listOf(song)))
                }.setNegativeButton(cancel) { di, _ ->
                    di.dismiss()
                }.create()
    }
}