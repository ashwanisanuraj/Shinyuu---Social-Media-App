package com.xero.shinyuu.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.R
import com.xero.shinyuu.adapters.ConnectAdapter
import com.xero.shinyuu.adapters.PostAdapter
import com.xero.shinyuu.databinding.FragmentFeedBinding
import com.xero.shinyuu.utils.CONNECT
import com.xero.shinyuu.utils.POST

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private var postList = ArrayList<Post>()
    private lateinit var postAdapter: PostAdapter
    private var connectList = ArrayList<User>()
    private lateinit var connectAdapter: ConnectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        postAdapter = PostAdapter(requireContext(), postList)
        binding.postRv.adapter = postAdapter
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())


        connectAdapter = ConnectAdapter(requireContext(), connectList)
        binding.connectRv.adapter = connectAdapter
        binding.connectRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)



        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + CONNECT).get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                connectList.clear()
                for (i in it.documents){
                    var user:User=i.toObject<User>()!!
                    tempList.add(user)
                }
                connectList.addAll(tempList)
                connectAdapter.notifyDataSetChanged()
            }

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
