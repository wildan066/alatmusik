package com.example.toko.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.toko.R
import com.example.toko.model.Shop

class ShopListAdapter(
    private val onItemClickListener: (Shop) -> Unit
): ListAdapter<Shop,ShopListAdapter.ShopViewHolder>(WORDS_COMPARATOR) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        return ShopViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val Shop =getItem(position)
        holder.bind(Shop)
        holder.itemView.setOnClickListener{
            onItemClickListener(Shop)
        }
    }

    class ShopViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        private val priceTextView: TextView =itemView.findViewById(R.id.priceTextView)
        private val noteTextView: TextView =itemView.findViewById(R.id.noteTextView)


        fun bind(shop: Shop?){
        nameTextView.text=shop?.name
        priceTextView.text= shop?.price
        noteTextView.text=shop?.note

        }
        companion object {
            fun create(parent: ViewGroup): ShopListAdapter.ShopViewHolder {

                val view :View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shop, parent,false)
                return ShopViewHolder(view)
            }
        }

    }

    companion object{
        private val WORDS_COMPARATOR = object :DiffUtil.ItemCallback<Shop>(){
            override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
                return oldItem.id==newItem.id
            }
        }
    }

}


