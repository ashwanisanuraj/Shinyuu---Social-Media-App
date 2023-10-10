package com.xero.shinyuu.Post

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.HomeActivity
import com.xero.shinyuu.Models.Post
import com.xero.shinyuu.databinding.ActivityPostBinding
import com.xero.shinyuu.fragments.AddFragment
import com.xero.shinyuu.fragments.MyPostFragment
import com.xero.shinyuu.fragments.ProfileFragment
import com.xero.shinyuu.utils.POST
import com.xero.shinyuu.utils.POST_FOLDER
import com.xero.shinyuu.utils.uploadImage

class PostActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }

    private var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) {
                url ->
                if (url != null) {
                    binding.postImageButton.setImageURI(uri)
                    imageUrl = url
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
        }

        binding.postImageButton.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.postButton.setOnClickListener {

            //Firebase.firestore.collection(USER_NODE).document().get()
            //.addOnSuccessListener {
            //var user = it.toObject<User>()!!

            val post: Post = Post(imageUrl!!, binding.caption.editText?.text.toString(),uid = Firebase.auth.currentUser!!.uid, time = System.currentTimeMillis().toString())

            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                //Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                //.set(post)
                //.addOnSuccessListener {
                //    startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                Toast.makeText(
                    this@PostActivity,
                    "Post successfully added",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }.addOnFailureListener { e ->
                // Handle the failure and display an error message
                Toast.makeText(
                    this@PostActivity,
                    "Error adding post: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                //finish()
                //}
            }
        }
    }
}
//}