package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCardCount: EditText
    private lateinit var btnStartGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCardCount = findViewById(R.id.editTextCardCount)
        btnStartGame = findViewById(R.id.btnStartGame)

        btnStartGame.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        val cardCountInput = editTextCardCount.text.toString().toIntOrNull()

        if (cardCountInput != null && cardCountInput >= 2 && cardCountInput % 2 == 0) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("cardCount", cardCountInput)
            startActivity(intent)
        } else {
            editTextCardCount.error = "Введите чётное число!"
        }
    }
}
