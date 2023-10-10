package com.xero.shinyuu.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.R
import com.xero.shinyuu.databinding.ConnectRvBinding

class ConnectAdapter(var context: Context, private var connectList: ArrayList<User>) :
    RecyclerView.Adapter<ConnectAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ConnectRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ConnectRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return connectList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(connectList.get(position).image).placeholder(R.drawable.user)
            .into(holder.binding.profilePic)
        holder.binding.textFieldUsername.text=connectList.get(position).name
    }
}