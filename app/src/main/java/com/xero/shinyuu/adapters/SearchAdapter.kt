package com.xero.shinyuu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.R
import com.xero.shinyuu.databinding.SearchRvBinding
import com.xero.shinyuu.utils.CONNECT

class SearchAdapter(var context: Context, private var userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isconnect = false

        val user = userList[position] // Accessing the user from the userList
        Glide.with(context)
            .load(user.image)
            .placeholder(R.drawable.user)
            .into(holder.binding.profilePic)
        holder.binding.textFieldUsername.text = user.name

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + CONNECT)
            .whereEqualTo("email", userList.get(position).email).get().addOnSuccessListener {
                if (it.documents.size==0){
                    isconnect=false

                }else{
                    holder.binding.connectButton.text="STOP"
                    isconnect=true
                }
        }
        holder.binding.connectButton.setOnClickListener {
            if (isconnect){
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + CONNECT)
                    .whereEqualTo("email", userList.get(position).email).get().addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + CONNECT).document(it.documents.get(0).id).delete()
                        holder.binding.connectButton.text="CONNECT"
                        isconnect=false
                    }


            }else{
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + CONNECT).document()
                    .set(userList.get(position))
                holder.binding.connectButton.text = "STOP"
                isconnect=true

            }

        }
    }

}
