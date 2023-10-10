package com.xero.shinyuu.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.adapters.MyPostRvAdapter
import com.xero.shinyuu.databinding.FragmentMyPostBinding
import com.xero.shinyuu.utils.POST

class MyPostFragment : Fragment() {
    private var currentUser: User? = null
    private var postList = ArrayList<Post>()
    private lateinit var binding:FragmentMyPostBinding
    private lateinit var adapter: MyPostRvAdapter

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPostBinding.inflate(inflater, container, false)

        adapter = MyPostRvAdapter(requireContext(), currentUser, postList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        Firebase.firestore.collection(POST).get()
            .addOnSuccessListener { querySnapshot ->
                val tempList = arrayListOf<Post>()
                val currentUserId = Firebase.auth.currentUser!!.uid // Get current user's UID

                for (document in querySnapshot.documents) {
                    val post = document.toObject<Post>()
                    if (post != null && post.uid == currentUserId) { // Filter posts by the current user
                        tempList.add(post)
                    }
                }

                postList.clear()
                postList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }

        return binding.root
    }

}