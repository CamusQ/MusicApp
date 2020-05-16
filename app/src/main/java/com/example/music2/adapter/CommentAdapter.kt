package com.example.music2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.activity.AudioActivity
import com.example.music2.entity.Comment
import com.example.music2.viewModel.CommentViewModel
import com.example.music2.viewModel.SlaveCommentViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.comment_cell.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class CommentAdapter(audioActivity: AudioActivity) :
    ListAdapter<Comment, CommentAdapter.ViewHolder>(DIFFCALLBACK) {

    lateinit var context: Context
    val slaveCommentAdapter by lazy { SlaveCommentAdapter(audioActivity) }
    val commViewModel by lazy { ViewModelProvider(audioActivity).get(CommentViewModel::class.java) }
    val slaveCommentViewModel by lazy { ViewModelProvider(audioActivity).get(SlaveCommentViewModel::class.java) }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userHeader: CircleImageView = itemView.findViewById(R.id.comm_userHeader)
        val nickName: TextView = itemView.findViewById(R.id.comm_nickName)
        val commDate: TextView = itemView.findViewById(R.id.comm_date)
        val masterComm: TextView = itemView.findViewById(R.id.master_comment)

//        val slaveCommRecycleView: RecyclerView = itemView.findViewById(R.id.slave_comm_recycleView)

    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(old: Comment, aNew: Comment): Boolean {
            return old === aNew
        }

        override fun areContentsTheSame(old: Comment, aNew: Comment): Boolean {
            return old.id == aNew.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_cell, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener {
            Log.d("comm_item:", it.comm_nickName.text.toString())
            Log.d("TAG", JSON.toJSONString(slaveCommentViewModel.slaveComms))
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val date = SimpleDateFormat("MM-dd").format(item.commTime)
        holder.commDate.text = date
        holder.masterComm.text = item.commContent
        holder.nickName.text = item.commName
        Glide.with(context).load(item.headerImageUrl).into(holder.userHeader)

//        holder.slaveCommRecycleView.adapter = slaveCommentAdapter
//        holder.slaveCommRecycleView.layoutManager = GridLayoutManager(context, 1)

        if (item.replyCommId != 0) {
            return
        }
        MainScope().launch {
            if (item.id != null && item.replyCommId != null) {
                slaveCommentViewModel.getSlaveComms(item.replyCommId!!, item.id!!)
            }
        }
    }

}