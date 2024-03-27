package com.xero.shinyuu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.adapters.PostAdapter
import com.xero.shinyuu.databinding.FragmentFeedBinding
import com.xero.shinyuu.utils.CONNECT
import com.xero.shinyuu.utils.POST
import com.xero.shinyuu.utils.USER_NODE

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private var postList = ArrayList<Post>()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        postAdapter = PostAdapter(requireContext(), postList)
        binding.postRv.adapter = postAdapter
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())


        // Fetch posts from Firestore
        Firebase.firestore.collection(POST).get().addOnSuccessListener { querySnapshot ->
            val tempList = ArrayList<Post>()

            for (document in querySnapshot.documents) {
                val post = document.toObject<Post>()
                if (post != null) {
                    tempList.add(post)
                }
            }

            postList.clear()
            postList.addAll(tempList)
            postAdapter.notifyDataSetChanged()
        }

        return binding.root
    }
}
