package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe.databinding.ActivityBasicBinding

class Basic : AppCompatActivity() {

    private lateinit var binding: ActivityBasicBinding
    private var player = "p1"

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.back.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        }
        binding.resetButton.visibility = View.GONE

        // Access the MediaPlayer instance from the singleton
        val musicMediaPlayer = MediaPlayerSingleton.musicMediaPlayer
        musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }

        binding.apply {
            box1.setOnClickListener { onButtonClick(box1) }
            box2.setOnClickListener { onButtonClick(box2) }
            box3.setOnClickListener { onButtonClick(box3) }
            box4.setOnClickListener { onButtonClick(box4) }
            box5.setOnClickListener { onButtonClick(box5) }
            box6.setOnClickListener { onButtonClick(box6) }
            box7.setOnClickListener { onButtonClick(box7) }
            box8.setOnClickListener { onButtonClick(box8) }
            box9.setOnClickListener { onButtonClick(box9) }
            resetButton.setOnClickListener { resetGame() }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun onButtonClick(button: Button) {
        if (button.text == "" && player == "p1") {
            button.background = ContextCompat.getDrawable(this, R.drawable.cross2)
            button.text = "x"
            button.isClickable = false
            win()
            if (!isGameWon()) {
                player = "cpu"
                cpuTurn()
            }
        }
    }

    private fun cpuTurn() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val availableButtons = listOf(binding.box1, binding.box2, binding.box3, binding.box4, binding.box5, binding.box6, binding.box7, binding.box8, binding.box9)
                .filter { it.text == "" }

            val winMove = findWinningMove()
            if (winMove != null) {
                winMove.background = ContextCompat.getDrawable(this, R.drawable.o2)
                winMove.text = "o"
                winMove.isClickable = false
            } else {
                val blockMove = findBlockingMove()
                if (blockMove != null) {
                    blockMove.background = ContextCompat.getDrawable(this, R.drawable.o2)
                    blockMove.text = "o"
                    blockMove.isClickable = false
                } else if (availableButtons.isNotEmpty()) {
                    val randomButton = availableButtons.random()
                    randomButton.background = ContextCompat.getDrawable(this, R.drawable.o2)
                    randomButton.text = "o"
                    randomButton.isClickable = false
                }
            }
            player = "p1"
            win()
        }, 500)
    }

    private fun findWinningMove(): Button? {
        binding.apply {
            // Horizontal
            if (box1.text == "o" && box2.text == "o" && box3.text == "") return box3
            if (box1.text == "o" && box2.text == "" && box3.text == "o") return box2
            if (box1.text == "" && box2.text == "o" && box3.text == "o") return box1
            if (box4.text == "o" && box5.text == "o" && box6.text == "") return box6
            if (box4.text == "o" && box5.text == "" && box6.text == "o") return box5
            if (box4.text == "" && box5.text == "o" && box6.text == "o") return box4
            if (box7.text == "o" && box8.text == "o" && box9.text == "") return box9
            if (box7.text == "o" && box8.text == "" && box9.text == "o") return box8
            if (box7.text == "" && box8.text == "o" && box9.text == "o") return box7
            // Vertical
            if (box1.text == "o" && box4.text == "o" && box7.text == "") return box7
            if (box1.text == "o" && box4.text == "" && box7.text == "o") return box4
            if (box1.text == "" && box4.text == "o" && box7.text == "o") return box1
            if (box2.text == "o" && box5.text == "o" && box8.text == "") return box8
            if (box2.text == "o" && box5.text == "" && box8.text == "o") return box5
            if (box2.text == "" && box5.text == "o" && box8.text == "o") return box2
            if (box3.text == "o" && box6.text == "o" && box9.text == "") return box9
            if (box3.text == "o" && box6.text == "" && box9.text == "o") return box6
            if (box3.text == "" && box6.text == "o" && box9.text == "o") return box3
            // Diagonal
            if (box1.text == "o" && box5.text == "o" && box9.text == "") return box9
            if (box1.text == "o" && box5.text == "" && box9.text == "o") return box5
            if (box1.text == "" && box5.text == "o" && box9.text == "o") return box1
            if (box3.text == "o" && box5.text == "o" && box7.text == "") return box7
            if (box3.text == "o" && box5.text == "" && box7.text == "o") return box5
            if (box3.text == "" && box5.text == "o" && box7.text == "o") return box3
        }
        return null
    }

    private fun findBlockingMove(): Button? {
        binding.apply {
            // Horizontal
            if (box1.text == "x" && box2.text == "x" && box3.text == "") return box3
            if (box1.text == "x" && box2.text == "" && box3.text == "x") return box2
            if (box1.text == "" && box2.text == "x" && box3.text == "x") return box1
            if (box4.text == "x" && box5.text == "x" && box6.text == "") return box6
            if (box4.text == "x" && box5.text == "" && box6.text == "x") return box5
            if (box4.text == "" && box5.text == "x" && box6.text == "x") return box4
            if (box7.text == "x" && box8.text == "x" && box9.text == "") return box9
            if (box7.text == "x" && box8.text == "" && box9.text == "x") return box8
            if (box7.text == "" && box8.text == "x" && box9.text == "x") return box7
            // Vertical
            if (box1.text == "x" && box4.text == "x" && box7.text == "") return box7
            if (box1.text == "x" && box4.text == "" && box7.text == "x") return box4
            if (box1.text == "" && box4.text == "x" && box7.text == "x") return box1
            if (box2.text == "x" && box5.text == "x" && box8.text == "") return box8
            if (box2.text == "x" && box5.text == "" && box8.text == "x") return box5
            if (box2.text == "" && box5.text == "x" && box8.text == "x") return box2
            if (box3.text == "x" && box6.text == "x" && box9.text == "") return box9
            if (box3.text == "x" && box6.text == "" && box9.text == "x") return box6
            if (box3.text == "" && box6.text == "x" && box9.text == "x") return box3
            // Diagonal
            if (box1.text == "x" && box5.text == "x" && box9.text == "") return box9
            if (box1.text == "x" && box5.text == "" && box9.text == "x") return box5
            if (box1.text == "" && box5.text == "x" && box9.text == "x") return box1
            if (box3.text == "x" && box5.text == "x" && box7.text == "") return box7
            if (box3.text == "x" && box5.text == "" && box7.text == "x") return box5
            if (box3.text == "" && box5.text == "x" && box7.text == "x") return box3
        }
        return null
    }

    private fun isGameWon(): Boolean {
        binding.apply {
            return ( // Horizontal
                    (box1.text == "x" && box2.text == "x" && box3.text == "x") ||
                            (box4.text == "x" && box5.text == "x" && box6.text == "x") ||
                            (box7.text == "x" && box8.text == "x" && box9.text == "x") ||
                            // Vertical
                            (box1.text == "x" && box4.text == "x" && box7.text == "x") ||
                            (box2.text == "x" && box5.text == "x" && box8.text == "x") ||
                            (box3.text == "x" && box6.text == "x" && box9.text == "x") ||
                            // Diagonal
                            (box1.text == "x" && box5.text == "x" && box9.text == "x") ||
                            (box3.text == "x" && box5.text == "x" && box7.text == "x")
                    )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun win() {
        binding.apply {
            if ( // Horizontal
                (box1.text == "x" && box2.text == "x" && box3.text == "x") ||
                (box4.text == "x" && box5.text == "x" && box6.text == "x") ||
                (box7.text == "x" && box8.text == "x" && box9.text == "x") ||
                // Vertical
                (box1.text == "x" && box4.text == "x" && box7.text == "x") ||
                (box2.text == "x" && box5.text == "x" && box8.text == "x") ||
                (box3.text == "x" && box6.text == "x" && box9.text == "x") ||
                // Diagonal
                (box1.text == "x" && box5.text == "x" && box9.text == "x") ||
                (box3.text == "x" && box5.text == "x" && box7.text == "x")
            ) {
                tvResult.text = "Won The GAME!"
//                Toast.makeText(this@Basic, "X Won The Game!", Toast.LENGTH_SHORT).show()
                disableButtons()
            } else if ( // Horizontal
                (box1.text == "o" && box2.text == "o" && box3.text == "o") ||
                (box4.text == "o" && box5.text == "o" && box6.text == "o") ||
                (box7.text == "o" && box8.text == "o" && box9.text == "o") ||
                // Vertical
                (box1.text == "o" && box4.text == "o" && box7.text == "o") ||
                (box2.text == "o" && box5.text == "o" && box8.text == "o") ||
                (box3.text == "o" && box6.text == "o" && box9.text == "o") ||
                // Diagonal
                (box1.text == "o" && box5.text == "o" && box9.text == "o") ||
                (box3.text == "o" && box5.text == "o" && box7.text == "o")
            ) {
                tvResult.text = "Lost The GAME!"
//                Toast.makeText(this@Basic, "O Won The Game!", Toast.LENGTH_SHORT).show()
                disableButtons()
            } else if (
                box1.text != "" && box2.text != "" && box3.text != "" &&
                box4.text != "" && box5.text != "" && box6.text != "" &&
                box7.text != "" && box8.text != "" && box9.text != ""
            ) {
                tvResult.text = "Match Draw"
                disableButtons()
                resetButton.visibility = View.VISIBLE
//                Toast.makeText(this@Basic, "It's a Tie... LOL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableButtons() {
        binding.apply {
            box1.isClickable = false
            box2.isClickable = false
            box3.isClickable = false
            box4.isClickable = false
            box5.isClickable = false
            box6.isClickable = false
            box7.isClickable = false
            box8.isClickable = false
            box9.isClickable = false
            resetButton.visibility = View.VISIBLE
        }
    }

    private fun resetGame() {
        player = "p1"

        binding.apply {
            box1.setBackgroundResource(R.drawable.box2)
            box2.setBackgroundResource(R.drawable.box2)
            box3.setBackgroundResource(R.drawable.box2)
            box4.setBackgroundResource(R.drawable.box2)
            box5.setBackgroundResource(R.drawable.box2)
            box6.setBackgroundResource(R.drawable.box2)
            box7.setBackgroundResource(R.drawable.box2)
            box8.setBackgroundResource(R.drawable.box2)
            box9.setBackgroundResource(R.drawable.box2)

            box1.text = ""
            box2.text = ""
            box3.text = ""
            box4.text = ""
            box5.text = ""
            box6.text = ""
            box7.text = ""
            box8.text = ""
            box9.text = ""

            box1.isClickable = true
            box2.isClickable = true
            box3.isClickable = true
            box4.isClickable = true
            box5.isClickable = true
            box6.isClickable = true
            box7.isClickable = true
            box8.isClickable = true
            box9.isClickable = true

            tvResult.text = ""
            resetButton.visibility = View.GONE
        }
    }
    override fun onPause() {
        super.onPause()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }
}