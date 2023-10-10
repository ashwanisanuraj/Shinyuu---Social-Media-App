package com.xero.shinyuu

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //transparent statusbar
        window.statusBarColor=Color.TRANSPARENT

        //we need to put delay and then show the signup page
        //we use 'Handler' to produce delay and go to next screen
        //by default the handler is depricated but the looper handler is not depreicated
        Handler(Looper.getMainLooper()).postDelayed({
            //here we will define our new screen
            //intent is a special type class where we define where we r going from one screen
            if ( FirebaseAuth.getInstance().currentUser == null)
                startActivity(Intent(this, signupActivity::class.java)) //intent from this page to signup page
            else
                startActivity(Intent(this,HomeActivity::class.java))
            //to stop user from coming back to splash screen
            finish()
        }, 1500)
    }
}