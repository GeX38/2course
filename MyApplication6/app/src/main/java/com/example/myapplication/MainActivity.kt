package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextRange: EditText = findViewById(R.id.editTextRange)
        val buttonStartGame: Button = findViewById(R.id.buttonStartGame)

        buttonStartGame.setOnClickListener {
            val range = editTextRange.text.toString()

            if (isValidRange(range)) {
                val (rangeStart, rangeEnd) = parseRange(range)
                if (rangeStart < rangeEnd) {
                    val intent = Intent(this, GuessingActivity::class.java)
                    intent.putExtra("rangeStart", rangeStart)
                    intent.putExtra("rangeEnd", rangeEnd)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Некорректный диапазон. Первое число должно быть меньше второго.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Некорректный формат диапазона. Используйте формат, например, 1-100.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidRange(range: String): Boolean {
        val regex = """^\d+-\d+$""".toRegex()
        return range.matches(regex)
    }

    private fun parseRange(range: String): Pair<Int, Int> {
        val (start, end) = range.split("-")
        return Pair(start.toInt(), end.toInt())
    }
}
