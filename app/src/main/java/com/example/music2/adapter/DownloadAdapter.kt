package com.example.music2.adapter

import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.music2.R
import com.example.music2.entity.AudioItem
import com.example.music2.utils.showToast
import kotlinx.android.synthetic.main.download_cell.*
import kotlinx.android.synthetic.main.download_cell.view.*

class DownloadAdapter : ListAdapter<AudioItem, DownloadAdapter.ViewHolder>(DIFFCALLBACK) {

    val mediaPlayer = MediaPlayer()

    object DIFFCALLBACK : DiffUtil.ItemCallback<AudioItem>() {
        override fun areContentsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areItemsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
            return oldItem === newItem
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val local_album_path: TextView = itemView.findViewById(R.id.album_Name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.download_cell, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.delete_album.setOnClickListener {
            Log.d("TAG","点击删除按钮")
        }

        holder.itemView.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            Log.d("TAG", "点击了 " + item.path)

            mediaPlayer.apply {
                setDataSource(item.path)
                prepareAsync()
                setOnPreparedListener {
                    Log.d("TAG","已加载完成" + item.path)
                    start()
                }
            }


        }

        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val downloadItem = getItem(position)
        holder.local_album_path.text =
            downloadItem.path.substring(downloadItem.path.lastIndexOf("/") + 1)
    }

}