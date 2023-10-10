package com.xero.shinyuu

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xero.shinyuu.Models.User
import com.xero.shinyuu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //to change the color of login and make it pressable in signup page
        val text =
            "<font color=#FFFFFFFF >New here? </font> <font color = #1E88E5> Register </font>"
        binding.signup.text = Html.fromHtml(text)

        window.statusBarColor = Color.TRANSPARENT

        binding.loginBtn.setOnClickListener {
            if (binding.textFieldEmail.editText?.text.toString().equals("") or
                binding.textFieldPassword.editText?.text.toString().equals("")
            ) {
                Toast.makeText(this@LoginActivity, "Please Fill All Fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var user = User(
                    binding.textFieldEmail.editText?.text.toString(),
                    binding.textFieldPassword.editText?.text.toString()
                )

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }


        binding.signup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, signupActivity::class.java))
            finish()
        }

    }
}