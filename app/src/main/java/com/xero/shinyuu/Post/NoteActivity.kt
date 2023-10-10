package com.xero.shinyuu.Post

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xero.shinyuu.R

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        window.statusBarColor= Color.TRANSPARENT


    }
}