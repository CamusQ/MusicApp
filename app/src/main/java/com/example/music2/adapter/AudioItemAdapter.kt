package com.example.music2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.activity.AudioActivity
import com.example.music2.entity.AlbumItem
import de.hdodenhof.circleimageview.CircleImageView

class AudioItemAdapter(val context: Context, val albumItemList : List<AlbumItem>) : RecyclerView.Adapter<AudioItemAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val audioName = itemView.findViewById<TextView>(R.id.audio_name)
        val audioImage = itemView.findViewById<ImageView>(R.id.img_playing_audio)
        val userHeader : CircleImageView = itemView.findViewById(R.id.userHeader)
        val nickName : TextView = itemView.findViewById(R.id.nickName)
        val contentDesc : TextView = itemView.findViewById(R.id.contentDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val albumItem = albumItemList.get(holder.adapterPosition)
            val intent = Intent(parent.context,AudioActivity::class.java).apply {
                putExtra(AudioActivity.ALBUM_ID,albumItem.id)
                putExtra(AudioActivity.AUDIO_NAME,albumItem.producerName + " · " + albumItem.albumName)
                putExtra(AudioActivity.AUDIO_IMAGE,albumItem.albumCoverUrl)
                putExtra(AudioActivity.AUDIO_URL,albumItem.mediaUrl)
                putExtra(AudioActivity.AUDIO_LRC_URL,albumItem.mediaLrcUrl)
            }
            parent.context.startActivity(intent)
        }

        return holder
    }

    override fun getItemCount() = albumItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumItemList[position]
        holder.audioName.text = album.producerName + " · " + album.albumName
        holder.nickName.text = album.producerNickname
        holder.contentDesc.text = album.contentDesc
        Glide.with(context).load(album.headerIcon).into(holder.userHeader)
        Glide.with(context).load(album.albumCoverUrl).into(holder.audioImage)
    }

}