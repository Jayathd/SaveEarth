package com.example.saveearth

import GameView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    var gameView: GameView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)
    }
}
