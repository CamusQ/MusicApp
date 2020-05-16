package com.example.music2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.music2.R
import com.example.music2.activity.AudioActivity
import com.example.music2.entity.Comment
import com.example.music2.viewModel.CommentViewModel
import com.example.music2.viewModel.SlaveCommentViewModel

class SlaveCommentAdapter(audioActivity: AudioActivity) :
    ListAdapter<Comment, SlaveCommentAdapter.ViewHolder>(DIFFCALLBACK) {

    val viewModel by lazy { ViewModelProvider(audioActivity).get(CommentViewModel::class.java) }
    val slaveCommentViewModel by lazy { ViewModelProvider(audioActivity).get(SlaveCommentViewModel::class.java) }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slaveCommUserNameLeft = itemView.findViewById<TextView>(R.id.slave_comm_username_left)
        val slaveCommUserNameRight = itemView.findViewById<TextView>(R.id.slave_comm_username_right)
        //回复
        val commReply = itemView.findViewById<TextView>(R.id.comm_reply)
        // 冒号
        val colon = itemView.findViewById<TextView>(R.id.colon)
        val commContent = itemView.findViewById<TextView>(R.id.slave_comm_content)
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlaveCommentAdapter.ViewHolder {
        Log.d("TAG", "SlaveCommentAdapter 的 onCreateViewHolder")

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.slave_comment_cell, parent, false)
        val holder = ViewHolder(view)

        return holder
    }

    override fun onBindViewHolder(holder: SlaveCommentAdapter.ViewHolder, position: Int) {

        Log.d("TAG", "SlaveCommentAdapter 的 onBindViewHolder")

        val item = getItem(position)
        //按照reply_comm_id查找name ，如果reply_comm_id 为0，代表主评论，如果不为0，代表回复的评论编号，再按照评论编号查找name

        if (item.replyCommId == 0) {
            holder.slaveCommUserNameRight.visibility = View.GONE
            holder.commReply.visibility = View.GONE
            holder.commContent.text = item.commContent
            return
        }

        holder.slaveCommUserNameLeft.text = item.commName

        val slaveComms = slaveCommentViewModel.slaveComms.value
        if (slaveComms != null) {
            for (comment in slaveComms) {
                if (item.replyCommId == comment.id) {
                    holder.slaveCommUserNameRight.text = comment.commName
                    holder.commContent.text = item.commContent
                }
            }
        }
    }
}