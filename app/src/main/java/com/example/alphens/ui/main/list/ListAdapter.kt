package com.example.alphens.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alphens.data.model.Note
import com.example.alphens.databinding.ListItemLayoutBinding
import com.example.alphens.util.hide
import java.text.SimpleDateFormat
import javax.inject.Inject

class ListAdapter @Inject constructor (
    private val onItemClicked: (Int, Note) -> Unit
): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    val sdf = SimpleDateFormat("dd MMM yyyy")
    private var list: MutableList<Note> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: MutableList<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ListItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.title.setText(item.title)
            binding.date.setText(sdf.format(item.date))
            binding.desc.apply {
                if (item.description.length > 120) {
                    text = "${item.description.substring(0, 120)}..."
                } else {
                    text = item.description
                }
            }
            binding.itemLayout.setOnClickListener {
                onItemClicked.invoke(
                    adapterPosition, item
                )
            }
        }
    }
}