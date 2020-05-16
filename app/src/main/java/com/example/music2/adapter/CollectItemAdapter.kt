package com.example.music2.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.activity.AudioActivity
import com.example.music2.entity.CollectItem
import com.example.music2.utils.showToast
import de.hdodenhof.circleimageview.CircleImageView

class CollectItemAdapter(val context: Context, val collectItemList: List<CollectItem>) :
    RecyclerView.Adapter<CollectItemAdapter.ViewHolder>() {

    val editor = context.getSharedPreferences("collect_folder", Context.MODE_PRIVATE).edit()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerIcon = itemView.findViewById<CircleImageView>(R.id.user_header)
        val singName = itemView.findViewById<TextView>(R.id.singerName)
        val albumItem = itemView.findViewById<TextView>(R.id.albumName)
        val collect_botton = itemView.findViewById<ImageView>(R.id.collect_button)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.collect_item, parent, false)
        val holder = ViewHolder(view)
        val prefs = context.getSharedPreferences("collect_folder", Context.MODE_PRIVATE)


        holder.itemView.setOnClickListener {
            val collectItem = collectItemList[holder.adapterPosition]
            val intent = Intent(context, AudioActivity::class.java).apply {
                putExtra(AudioActivity.ALBUM_ID,collectItem.id)
                putExtra(
                    AudioActivity.AUDIO_NAME,
                    collectItem.producerName + " · " + collectItem.albumName
                )
                putExtra(AudioActivity.AUDIO_IMAGE, collectItem.albumCoverUrl)
                putExtra(AudioActivity.AUDIO_URL, collectItem.mediaUrl)
                putExtra(AudioActivity.AUDIO_LRC_URL, collectItem.mediaLrcUrl)
            }
            context.startActivity(intent)
        }

        holder.collect_botton.setOnClickListener {
            val collectItem = collectItemList[holder.adapterPosition]
            if (prefs.getBoolean("" + collectItem.id, false)) {
                holder.collect_botton.setBackgroundResource(R.drawable.dislike)
                editor.putBoolean("" + collectItem.id, false)
                editor.apply()
                "取消收藏".showToast(context)
            } else {
                holder.collect_botton.setBackgroundResource(R.drawable.like)
                editor.putBoolean("" + collectItem.id, true)
                editor.apply()
                "加入收藏夹".showToast(context)
            }

        }

        return holder
    }

    override fun getItemCount() = collectItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collectItem = collectItemList[position]
        Glide.with(context).load(collectItem.headerIcon).into(holder.headerIcon)
        holder.singName.text = collectItem.producerName
        holder.albumItem.text = collectItem.albumName


//        if (collectItem.collected) {
        holder.collect_botton.setBackgroundResource(R.drawable.like)
        editor.putBoolean("" + collectItem.id, true)
        editor.apply()

//        } else {
//            holder.collect_botton.setBackgroundResource(R.drawable.dislike)
//            editor.putBoolean("" + collectItem.id, false)
//            editor.apply()
//        }
    }

}