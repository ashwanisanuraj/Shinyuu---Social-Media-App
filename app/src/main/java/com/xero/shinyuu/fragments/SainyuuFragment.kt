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
import com.xero.shinyuu.Models.User // Import the correct User class
import com.xero.shinyuu.adapters.SearchAdapter
import com.xero.shinyuu.databinding.FragmentSainyuuBinding
import com.xero.shinyuu.utils.USER_NODE

class SainyuuFragment : Fragment() {
    lateinit var binding: FragmentSainyuuBinding
    private lateinit var adapter: SearchAdapter
    private var userList = ArrayList<User>()
    private var connectList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSainyuuBinding.inflate(inflater, container, false)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter




        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            val tempList = ArrayList<User>()
            userList.clear()
            for (i in it.documents) {
                if (i.id.toString() == Firebase.auth.currentUser!!.uid.toString()) {

                } else {
                    val user: User = i.toObject<User>()!! // Use the correct User class
                    tempList.add(user)
                }


            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        binding.searchButton.setOnClickListener {
            val text = binding.searchView.text.toString()
            if (text.isNotEmpty()) {
                Firebase.firestore.collection(USER_NODE)
                    .whereEqualTo("name", text)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val tempList = ArrayList<User>()
                        userList.clear()
                        for (document in querySnapshot.documents) {
                            val user: User = document.toObject<User>()!!
                            tempList.add(user)
                        }
                        userList.addAll(tempList)
                        adapter.notifyDataSetChanged()
                    }
            }
        }
        return binding.root
    }
}
