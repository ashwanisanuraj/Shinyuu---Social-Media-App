package com.xero.shinyuu.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.Post.PostActivity
import com.xero.shinyuu.adapters.ViewPagerAdapter
import com.xero.shinyuu.databinding.FragmentProfileBinding
import com.xero.shinyuu.signupActivity
import com.xero.shinyuu.utils.USER_NODE

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.AddPostButton.setOnClickListener{
            val intent = Intent(activity, PostActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, signupActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }
        viewPagerAdapter=ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(), "My Posts")
        viewPagerAdapter.addFragments(NotesFragment(), "My Note")
        binding.viewPager.adapter=viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.textFieldUsername.text = user.name
                binding.bio.text = user.email
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profilePic)
                }
            }
    }
}