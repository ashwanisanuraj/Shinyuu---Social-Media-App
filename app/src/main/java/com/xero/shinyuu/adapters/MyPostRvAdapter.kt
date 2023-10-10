package com.xero.shinyuu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.R
import com.xero.shinyuu.databinding.MyPostRvDesignBinding
import com.xero.shinyuu.utils.POST_FOLDER
import com.xero.shinyuu.utils.USER_NODE

class MyPostRvAdapter(
    var context: Context,
    private val currentUser: User?,
    private val postList: ArrayList<Post>
) :
    RecyclerView.Adapter<MyPostRvAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Firebase.firestore.collection(USER_NODE).document().get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<User>()
                val currentUserId = Firebase.auth.currentUser!!.uid

                if (postList[position].uid == currentUserId) {

                    Glide.with(context)
                        .load(postList[position].postUrl)
                        .placeholder(R.drawable.loading)
                        .into(holder.binding.postImage)

                    holder.binding.caption.text = postList[position].caption
                    val text = TimeAgo.using(postList.get(position).time.toLong())
                    holder.binding.time2.text = text

                } else {
                    holder.binding.postImage.visibility = View.GONE
                    holder.binding.caption.visibility = View.GONE
                }


            }

    }
}





