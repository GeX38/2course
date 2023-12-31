package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private var cardValues = mutableListOf<Int>()
    private var selectedCards = mutableListOf<Int>()
    private var pairsFound = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        gridLayout = findViewById(R.id.gridLayout)

        val cardCount = intent.getIntExtra("cardCount", 0)
        initializeGame(cardCount)
        resetGame(cardCount)
        adjustCardSizes(cardCount)
    }


    private fun initializeGame(cardCount: Int) {
        for (i in 1..cardCount / 2) {
            cardValues.add(i)
            cardValues.add(i)
        }
        cardValues.shuffle()

        for (value in cardValues) {
            val cardView = createCardView(value, cardCount)
            gridLayout.addView(cardView)
        }

        adjustCardSizes(cardCount)
    }

    private fun createCardView(value: Int, cardCount: Int): View {
        val cardView = TextView(this)
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.width = getCardWidth()
        layoutParams.height = getCardHeight(cardCount)
        layoutParams.setMargins(2, 2, 2, 2)
        cardView.layoutParams = layoutParams
        cardView.text = ""
        cardView.setBackgroundResource(android.R.drawable.btn_default)
        cardView.setTextColor(Color.BLACK)
        cardView.gravity = Gravity.CENTER
        cardView.setOnClickListener {
            onCardClicked(it, value)
        }
        return cardView
    }

    private fun getCardWidth(): Int {
        val screenWidth = resources.displayMetrics.widthPixels
        val columns = gridLayout.columnCount
        return screenWidth / columns - 4
    }

    private fun getCardHeight(cardCount: Int): Int {
        val screenHeight = resources.displayMetrics.heightPixels - 56
        return (screenHeight / cardCount) * gridLayout.columnCount - 4
    }





    private fun onCardClicked(view: View, value: Int) {
        if (selectedCards.size < 2) {
            selectedCards.add(value)
            val cardView = view as TextView
            cardView.text = value.toString()

            if (selectedCards.size == 2) {
                if (selectedCards[0] == selectedCards[1]) {
                    pairsFound++
                    checkForWin()
                    Handler(Looper.getMainLooper()).postDelayed({
                        removeSelectedCards()
                    }, 500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        clearSelectedCards()
                    }, 500)
                }
            }
        }
    }

    private fun clearSelectedCards() {
        for (i in 0 until gridLayout.childCount) {
            val cardView = gridLayout.getChildAt(i) as? TextView
            cardView?.text = ""
        }
        selectedCards.clear()
    }

    private fun removeSelectedCards() {
        for (value in selectedCards) {
            for (i in 0 until gridLayout.childCount) {
                val cardView = gridLayout.getChildAt(i) as? TextView
                if (cardView?.text == value.toString()) {
                    animateCardDisappearance(cardView)
                }
            }
        }
        selectedCards.clear()
    }

    private fun animateCardDisappearance(cardView: TextView?) {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 500
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                cardView?.text = ""
                cardView?.isEnabled = false
                cardView?.setBackgroundColor(Color.TRANSPARENT)
            }
        })
        cardView?.startAnimation(fadeOut)
    }

    private fun checkForWin() {
        if (pairsFound == cardValues.size / 2) {
            showWinMessage()
        }
    }

    private fun showWinMessage() {
        Toast.makeText(this, "Поздравляем! Вы победили!", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun resetGame(cardCount: Int) {
        gridLayout.removeAllViews()
        cardValues.clear()
        selectedCards.clear()
        pairsFound = 0

        initializeGame(cardCount)
    }

    private fun adjustCardSizes(cardCount: Int) {
        if (cardCount <= 2) {
            gridLayout.columnCount = cardCount
            val screenWidth = resources.displayMetrics.widthPixels
            val cardWidth = (screenWidth - 10 * (cardCount + 1)) / cardCount

            for (i in 0 until gridLayout.childCount) {
                val child = gridLayout.getChildAt(i)
                if (child is TextView) {
                    val layoutParams = child.layoutParams as? GridLayout.LayoutParams
                    layoutParams?.width = cardWidth
                    child.layoutParams = layoutParams
                }
            }
            return
        }
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels - 56

        var columns = 1
        var rows = cardCount / columns

        var cardWidth = (screenWidth - 10 * (columns + 1)) / columns
        var cardHeight = getCardHeight(cardCount)

        while (rows * cardHeight > screenHeight) {
            columns++
            rows = (cardCount + columns - 1) / columns
            cardWidth = (screenWidth - 10 * (columns + 1)) / columns
            cardHeight = getCardHeight(cardCount)
        }

        gridLayout.columnCount = columns

        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is TextView) {
                val layoutParams = child.layoutParams as? GridLayout.LayoutParams
                layoutParams?.width = cardWidth
                layoutParams?.height = cardHeight
                child.layoutParams = layoutParams
            }
        }
    }




}
