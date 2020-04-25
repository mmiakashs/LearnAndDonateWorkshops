package com.nibri.simplegame

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val cellGrid: Array<Button> by lazy { arrayOf(btn_11, btn_12, btn_13, btn_21, btn_22, btn_23, btn_31, btn_32, btn_33) }
    private val linesGrid: Array<View> by lazy { arrayOf(line_0, line_1, line_2, line_3, line_4, line_5, line_6, line_7) }
    private val gameOverPaths = arrayOf("012", "345", "678", "036", "147", "258", "048", "246")
    private var isFirstPlayersTurn = true
    private var isPlayWithRandom = false
    private var isGameOver = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (b in cellGrid) b.setOnClickListener { onClickCell(it as Button) }
        btn_start.setOnClickListener { startTheGame(true) }
        btn_play_with_opponent.setOnClickListener { startTheGame(false) }

        // random game play for initial view
        for (i in 1..5) {
            isFirstPlayersTurn = !isFirstPlayersTurn
            cellGrid.filter { it.text.isNullOrEmpty() }.random().text = if (isFirstPlayersTurn) "X" else "O"
        }
    }

    // resets all the value to its starting point
    private fun startTheGame(withMobile: Boolean) {
        for (l in linesGrid) l.visibility = View.GONE
        for (c in cellGrid) c.text = ""
        isPlayWithRandom = withMobile
        isFirstPlayersTurn = true
        isGameOver = false
        tv_msgBox.text = ""
        btn_start.visibility = View.INVISIBLE
        btn_play_with_opponent.visibility = View.INVISIBLE
    }

    // on click of
    private fun onClickCell(cell: Button) {
        if (cell.text.isNullOrEmpty() && !isGameOver) {
            cell.text = if (isFirstPlayersTurn) "X" else "O"
            checkGameStatus()
            isFirstPlayersTurn = !isFirstPlayersTurn

            // if its opponents turn and playing with mobile
            if (isPlayWithRandom && !isFirstPlayersTurn) clickRandom()
        }
    }

    private fun clickRandom() {
        cellGrid.filter { it.text.isNullOrEmpty() }.let { if (it.isNotEmpty()) it.random().performClick() }
    }

    private fun checkGameStatus() {
        for (i in gameOverPaths.indices) {
            // filter out all empty cells
            // looking for any cell collection that match with any gameOver paths
            val checkedCells = cellGrid.filterIndexed { index, cell ->
                gameOverPaths[i].contains("$index") && cell.text == if (isFirstPlayersTurn) "X" else "O"
            }

            // checked cell count 3 means the game is over
            if (checkedCells.size == 3) {
                linesGrid[i].visibility = View.VISIBLE
                isGameOver = true
                break
            }
        }

        // checking for if the game is draw
        val isDraw = !isGameOver && cellGrid.none { it.text.isEmpty() }

        // setting appropriate message
        tv_msgBox.text = when {
            isGameOver -> "তুমি ${if (isFirstPlayersTurn) "জিতেছ !!!" else "হেরে গেছো :("}"
            isDraw -> "ম্যাচ ড্র হয়েছে, আবার চেষ্টা করো"
            else -> ""
        }

        // on game over, make the buttons visible
        if (isGameOver || isDraw) {
            btn_start.visibility = View.VISIBLE
            btn_play_with_opponent.visibility = View.VISIBLE
        }
    }
}
