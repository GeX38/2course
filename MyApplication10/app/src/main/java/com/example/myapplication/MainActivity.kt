package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNumber1: EditText
    private lateinit var editTextNumber2: EditText
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNumber1 = findViewById(R.id.editTextNumber1)
        editTextNumber2 = findViewById(R.id.editTextNumber2)
        textViewResult = findViewById(R.id.textViewResult)
    }

    fun onSumButtonClick(view: View) {
        val num1 = editTextNumber1.text.toString().toDoubleOrNull() ?: 0.0
        val num2 = editTextNumber2.text.toString().toDoubleOrNull() ?: 0.0
        val sum = num1 + num2
        textViewResult.text = "Результат: $sum"
    }
}
