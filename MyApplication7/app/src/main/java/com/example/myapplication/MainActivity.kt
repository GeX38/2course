package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout: GridLayout = findViewById(R.id.gridLayout)
        setupGridLayout(gridLayout)

        val btnShuffle: Button = findViewById(R.id.btnShuffle)
        btnShuffle.setOnClickListener {
            shuffleCells(gridLayout)
        }
    }

    private fun setupGridLayout(gridLayout: GridLayout) {
        val cellSize = resources.getDimensionPixelSize(R.dimen.cell_size)
        val borderSize = resources.getDimensionPixelSize(R.dimen.border_size)

        for (row in 0 until gridLayout.rowCount) {
            for (col in 0 until gridLayout.columnCount) {
                val cell = View(this)
                val params = GridLayout.LayoutParams()
                params.width = cellSize
                params.height = cellSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                cell.layoutParams = params

                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.setColor(if (Random.nextBoolean()) Color.BLUE else Color.MAGENTA)
                shape.setStroke(borderSize, Color.BLACK)
                cell.background = shape

                cell.setOnClickListener {
                    onCellClicked(row, col, gridLayout)
                }

                gridLayout.addView(cell)
            }
        }
    }

    private fun onCellClicked(row: Int, col: Int, gridLayout: GridLayout) {

        val clickedCell = gridLayout.getChildAt(row * gridLayout.columnCount + col)
        if (clickedCell is View) {
            changeCellColor(clickedCell)
        }

        for (c in 0 until gridLayout.columnCount) {
            val view = gridLayout.getChildAt(row * gridLayout.columnCount + c)
            if (view is View) {
                changeCellColor(view)
            }
        }

        for (r in 0 until gridLayout.rowCount) {
            val view = gridLayout.getChildAt(r * gridLayout.columnCount + col)
            if (view is View) {
                changeCellColor(view)
            }
        }

        checkForVictory(gridLayout)
    }

    private fun changeCellColor(cell: View) {
        val shape = cell.background as GradientDrawable
        val currentColor = shape.color?.defaultColor ?: Color.TRANSPARENT
        val oppositeColor = if (currentColor == Color.BLUE) Color.MAGENTA else Color.BLUE
        shape.setColor(oppositeColor)
    }

    private fun checkForVictory(gridLayout: GridLayout) {

        var allCellsSameColor = true
        val firstCell = gridLayout.getChildAt(0) as View

        for (i in 1 until gridLayout.childCount) {
            val currentCell = gridLayout.getChildAt(i) as View
            if (getColorOfCell(currentCell) != getColorOfCell(firstCell)) {
                allCellsSameColor = false
                break
            }
        }

        if (allCellsSameColor) {
            showToast("Поздравляю, вы выиграли!")
        }
    }

    private fun getColorOfCell(cell: View): Int {
        val shape = cell.background as GradientDrawable
        return shape.color?.defaultColor ?: Color.TRANSPARENT
    }

    private fun showToast(message: String) {

        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun shuffleCells(gridLayout: GridLayout) {
        for (i in 0 until gridLayout.childCount) {
            val cell = gridLayout.getChildAt(i)
            if (cell is View) {
                setRandomCellColor(cell)
            }
        }

        checkForVictory(gridLayout)
    }

    private fun setRandomCellColor(cell: View) {
        val shape = cell.background as GradientDrawable
        shape.setColor(if (Random.nextBoolean()) Color.BLUE else Color.MAGENTA)
    }
}
