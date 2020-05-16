package com.example.music2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.example.music2.R
import com.example.music2.activity.AudioActivity
import com.example.music2.entity.AlbumListenCounts

class RecentlyItemAdapter :ListAdapter<AlbumListenCounts,RecentlyItemAdapter.ViewHolder>(DIFFCALLBACK) {

    object DIFFCALLBACK : DiffUtil.ItemCallback<AlbumListenCounts>(){
        override fun areItemsTheSame(oldItem: AlbumListenCounts, newItem: AlbumListenCounts): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AlbumListenCounts, newItem: AlbumListenCounts): Boolean {
            return oldItem.album.id == newItem.album.id
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recSingerName : TextView = itemView.findViewById(R.id.rec_singerName)
        val recAlbumName : TextView = itemView.findViewById(R.id.rec_albumName)
        val listenCounts : TextView = itemView.findViewById(R.id.listen_counts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recently_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val recentItem = getItem(holder.adapterPosition)
            val intent = Intent(parent.context,AudioActivity::class.java).apply {
                putExtra(AudioActivity.ALBUM_ID,recentItem.album.id)
                putExtra(AudioActivity.AUDIO_NAME,recentItem.album.producerName + " · " + recentItem.album.albumName)
                putExtra(AudioActivity.AUDIO_IMAGE,recentItem.album.albumCoverUrl)
                putExtra(AudioActivity.AUDIO_URL,recentItem.album.mediaUrl)
                putExtra(AudioActivity.AUDIO_LRC_URL,recentItem.album.mediaLrcUrl)
            }
            parent.context.startActivity(intent)
        }
        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentlyItem = getItem(position)
        holder.recSingerName.text = recentlyItem.album.producerName
        holder.recAlbumName.text = recentlyItem.album.albumName
        holder.listenCounts.text = "已收听 "+recentlyItem.listenCounts + " 次"

    }

}