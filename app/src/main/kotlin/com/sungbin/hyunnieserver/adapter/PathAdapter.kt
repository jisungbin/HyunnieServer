package com.sungbin.hyunnieserver.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.hyunnieserver.databinding.LayoutPathBinding


/**
 * Created by SungBin on 2020-08-23.
 */

class PathAdapter(
    private val items: List<String>,
    private val activity: Activity
) : RecyclerView.Adapter<PathAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(path: String)
    }

    fun setOnClickListener(action: (String) -> Unit) {
        onClickListener = object : OnClickListener {
            override fun onClick(path: String) {
                action(path)
            }
        }
    }

    inner class ViewHolder(private val binding: LayoutPathBinding, private val activity: Activity) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(path: String, listener: OnClickListener?) {
            binding.tvPath.text = path
            binding.cvContainer.setOnClickListener {
                listener?.onClick(path)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutPathBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false),
            activity
        )

    override fun onBindViewHolder(@NonNull viewholder: ViewHolder, position: Int) {
        viewholder.bindViewHolder(items[position], onClickListener)
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position
}