package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class GuessingActivity : AppCompatActivity() {

    private lateinit var range: IntRange
    private var currentGuess: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guessing)

        val textViewQuestion: TextView = findViewById(R.id.textViewQuestion)
        val buttonHigher: Button = findViewById(R.id.buttonHigher)
        val buttonLower: Button = findViewById(R.id.buttonLower)
        val buttonGuessed: Button = findViewById(R.id.buttonGuessed)

        range = getIntRangeFromIntent()

        updateQuestion()

        buttonHigher.setOnClickListener {
            range = IntRange(currentGuess + 1, range.last)
            updateQuestion()
        }

        buttonLower.setOnClickListener {
            range = IntRange(range.first, currentGuess - 1)
            updateQuestion()
        }

        buttonGuessed.setOnClickListener {
            textViewQuestion.text = "Угадал! Загаданное число: $currentGuess"
            buttonHigher.isEnabled = false
            buttonLower.isEnabled = false
            buttonGuessed.isEnabled = false

            val playAgainButton = Button(this)
            playAgainButton.text = "Играть снова"
            playAgainButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)

            val layout = findViewById<RelativeLayout>(R.id.activity_guessing)
            layout.addView(playAgainButton, layoutParams)
        }
    }

    private fun getIntRangeFromIntent(): IntRange {
        val rangeStart = intent.getIntExtra("rangeStart", 1)
        val rangeEnd = intent.getIntExtra("rangeEnd", 100)
        return IntRange(rangeStart, rangeEnd)
    }

    private fun updateQuestion() {
        if (range.first <= range.last) {
            currentGuess = (range.first + range.last) / 2
            val textViewQuestion: TextView = findViewById(R.id.textViewQuestion)
            textViewQuestion.text = "Ваше число больше, меньше или равно $currentGuess?"
        }
    }
}
