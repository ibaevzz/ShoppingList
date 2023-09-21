package com.ibaevzz.shoppinglist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ibaevzz.shoppinglist.R
import com.ibaevzz.shoppinglist.databinding.ItemBinding
import com.ibaevzz.shoppinglist.network.models.AllItemsOfList
import com.ibaevzz.shoppinglist.network.models.Item

class ItemsAdapter(private var list: AllItemsOfList,
                   private val delete: (Long)->Unit,
                   private val cross: (Long)->Unit,
                   private val onClick: (Item)->Unit): Adapter<ItemsAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.items.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.name.text = list.items[position].name
        holder.binding.n.text = list.items[position].created
        holder.binding.delete.setOnClickListener{
            delete(list.items[position].id)
        }
        holder.binding.cross.setOnClickListener{
            if(!list.items[position].isCrossed){
                holder.binding.crossed.visibility = View.VISIBLE
                holder.binding.cross.setImageDrawable(holder.binding.root.context.getDrawable(R.drawable.baseline_visibility_24))
            }else{
                holder.binding.crossed.visibility = View.INVISIBLE
                holder.binding.cross.setImageDrawable(holder.binding.root.context.getDrawable(R.drawable.visibility_off))
            }
            cross(list.items[position].id)
        }
        holder.binding.item.setOnClickListener{
            onClick(list.items[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeItems(list: AllItemsOfList){
        this.list = list
        notifyDataSetChanged()
    }
}