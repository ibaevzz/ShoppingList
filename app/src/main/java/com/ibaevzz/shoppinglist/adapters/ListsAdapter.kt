package com.ibaevzz.shoppinglist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ibaevzz.shoppinglist.databinding.ListBinding
import com.ibaevzz.shoppinglist.network.models.AllLists

class ListsAdapter(private var lists: AllLists,
                   private val delete: (Long)->Unit,
                   private val onClick: (Long)->Unit): Adapter<ListsAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lists.lists.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.name.text = lists.lists[position].name
        holder.binding.created.text = lists.lists[position].created
        holder.binding.delete.setOnClickListener{
            delete(lists.lists[position].id)
        }
        holder.binding.list.setOnClickListener{
            onClick(lists.lists[position].id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeLists(lists: AllLists){
        this.lists = lists
        notifyDataSetChanged()
    }
}