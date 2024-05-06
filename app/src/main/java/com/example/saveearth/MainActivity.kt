package com.example.saveearth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    fun startGame(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish()
    }
}
