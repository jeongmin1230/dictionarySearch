package com.example.dictionarysearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_layout.view.*

class ListAdapter(val itemList: ArrayList<ListLayout>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.word.text = itemList[position].word
        holder.mean.text = itemList[position].mean

        val item = itemList[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> NAME : ${item.word}, NUMBER : ${item.mean}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag=item
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
        val word: TextView = itemView.findViewById(R.id.word)
        val mean: TextView = itemView.findViewById(R.id.mean)

        fun bind(listener: View.OnClickListener, item: ListLayout) {
            view.word.text = item.word
            view.mean.text = item.mean
            view.setOnClickListener(listener)
        }
    }
}