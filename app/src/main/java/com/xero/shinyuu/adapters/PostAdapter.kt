package com.xero.shinyuu.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.R
import com.xero.shinyuu.databinding.PostRvBinding
import com.xero.shinyuu.utils.USER_NODE

class PostAdapter(private val context: Context, private val postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(val binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
            }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    val timeInMillis = System.currentTimeMillis()

                    Glide.with(context)
                        .load(user?.image)
                        .placeholder(R.drawable.profile_icon)
                        .into(holder.binding.profilePic)

                    holder.binding.share.setOnClickListener{
                        var i = Intent(Intent.ACTION_SEND)
                        i.type ="text/plain"
                        i.putExtra(Intent.EXTRA_TEXT,postList[position].postUrl)
                        context.startActivity(i)
                    }

                    holder.binding.textFieldUsername.text = user?.name
                    holder.binding.email.text = user?.email
                    val text = TimeAgo.using(postList.get(position).time.toLong())
                    holder.binding.time.text = text

                    Glide.with(context)
                        .load(postList[position].postUrl)
                        .placeholder(R.drawable.loading)
                        .into(holder.binding.postImage)
                    holder.binding.caption.text = postList[position].caption
                    holder.binding.likeButton.setOnClickListener{
                        holder.binding.likeButton.setImageResource(R.drawable.like_image)
                    }
                }
            }
}

