package com.xero.shinyuu

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.databinding.ActivityMainBinding
import com.xero.shinyuu.databinding.ActivitySignupBinding
import com.xero.shinyuu.utils.USER_NODE
import com.xero.shinyuu.utils.USER_PROFILE_FOLDERS
import com.xero.shinyuu.utils.uploadImage

class signupActivity : AppCompatActivity() {
    //we will use binding, enable it in built.gradle
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    //when user clicks signup, we need to store the data
    lateinit var user: User


    //for image upload we need to open gallery. for that we need launcher
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //we r getting this with it by default, lets change it to uri
            uri ->
        uri?.let {
            //if uri is not null
            //we need a fn to upload image and then return its url
            //uri is uniform resource identifier, used to get resource from android
            //we need url of our image that is uploaded to firebase
            // for this we create a new package/kotlin class ( utils ), becuase we need to upload image again and again
            //after finishing Utils.kt
            uploadImage(uri, USER_PROFILE_FOLDERS) {
                if (it == null) {

                } else {
                    user.image = it
                    binding.profilePic.setImageURI(uri)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialize user
        user = User()

        if (intent.hasExtra("MODE")){
            if (intent.getIntExtra("MODE",-1)==1){
                binding.signUpBtn.text="Update"
                //binding.login.text="" //to hide the login button
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user = it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profilePic)
                        }
                        binding.textFieldUsername.editText?.setText(user.name)
                        binding.textFieldEmail.editText?.setText(user.email)
                        binding.textFieldPassword.editText?.setText(user.password)
                    }
            }
        }

        //to change the color of login and make it pressable in signup page
        val text =
            "<font color=#FFFFFFFF >Already have an acoount? </font> <font color = #1E88E5> Login </font>"
        binding.login.text = Html.fromHtml(text)

        window.statusBarColor = Color.TRANSPARENT

        binding.signUpBtn.setOnClickListener {
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@signupActivity, HomeActivity::class.java))
                            Toast.makeText(this@signupActivity, "Profile Update Successfull", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                }
            } else {
                if (binding.textFieldUsername.editText?.text.toString().equals("") or
                    binding.textFieldEmail.editText?.text.toString().equals("") or
                    binding.textFieldPassword.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@signupActivity,
                        "Please Fill All Fields",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.textFieldEmail.editText?.text.toString(),
                        binding.textFieldPassword.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.textFieldUsername.editText?.text.toString()
                            user.email = binding.textFieldEmail.editText?.text.toString()
                            user.password = binding.textFieldPassword.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@signupActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                    Toast.makeText(
                                        this@signupActivity,
                                        "Login Successfull",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                        } else {
                            Toast.makeText(
                                this@signupActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
            }
            binding.addImage.setOnClickListener {
                launcher.launch("image/*")
            }
            binding.login.setOnClickListener {
                startActivity(Intent(this@signupActivity, LoginActivity::class.java))
                finish()
            }

        }

        //user will need to provide 1.An image, Username, email, password
        //for this we will create a model
        // for circular image view we need to need to use dependencies from google 'circular image android'
        //for material3 text field, google 'material3 text field', same for material 3 button.
}
