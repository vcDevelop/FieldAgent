package com.example.fieldagent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ClientAdapter(private val clientIds: List<String>, private val onItemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clientId = clientIds[position]
        holder.bind(clientId)
    }

    override fun getItemCount(): Int {
        return clientIds.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(clientId: String) {
            itemView.setOnClickListener { onItemClickListener(clientId) }
            itemView.findViewById<TextView>(R.id.cust_name).text = clientId
        }
    }
}
