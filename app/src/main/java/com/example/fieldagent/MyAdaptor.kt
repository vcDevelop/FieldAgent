package com.example.fieldagent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdaptor(private val userList:ArrayList<User>):RecyclerView.Adapter<MyAdaptor.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cus_name:TextView = itemView.findViewById(R.id.cus_name)
        val instit_name:TextView = itemView.findViewById(R.id.instit_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
      return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cus_name.text=userList[position].cust_name
        holder.instit_name.text=userList[position].institute_name
    }
}