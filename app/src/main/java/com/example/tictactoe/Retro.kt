package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tictactoe.databinding.ActivityRetroBinding

class Retro : AppCompatActivity() {

    private lateinit var binding: ActivityRetroBinding
    private var player = "p1"
    private var isRainGifVisible = true
    private lateinit var rainMediaPlayer: MediaPlayer
    private lateinit var trainMediaPlayer: MediaPlayer
    private lateinit var clickMediaPlayer: MediaPlayer
    private var isMuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_retro)

        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)

        Glide.get(this).clearMemory()
        Thread {
            Glide.get(this).clearDiskCache()
        }.start()

        binding = ActivityRetroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Preload the GIF
        Glide.with(this)
            .asGif()
            .load(R.drawable.nights)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .preload()

        val typeWriterAnimation = TypeWriterAnimation(binding.appName, "Tic Tac Toe")
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                typeWriterAnimation.startAnimation(3000)
                handler.postDelayed(this, 7000)
            }
        }
        handler.post(runnable)

        binding.resetButton.visibility = View.GONE

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)

        Glide.with(this)
            .asGif()
            .load(R.drawable.nights)
            .into(binding.backgroundGif)

        val neonPulse = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)

        // Access the MediaPlayer instance from the singleton
        val musicMediaPlayer = MediaPlayerSingleton.musicMediaPlayer
        musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            Glide.with(this)
                .asGif()
                .load(R.drawable.rain2)
                .apply(requestOptions)
                .into(binding.rainGif)
        }, 1000)

        // Initialize and start the MediaPlayer for rain sound
        rainMediaPlayer = MediaPlayer.create(this, R.raw.rain)
        rainMediaPlayer.isLooping = false
        rainMediaPlayer.setOnCompletionListener {
            Handler(Looper.getMainLooper()).postDelayed({
                rainMediaPlayer.start()
            }, 1000) // 1 second delay
        }
        rainMediaPlayer.setVolume(0.5f, 0.5f)
        rainMediaPlayer.start()

        // Initialize and start the MediaPlayer for train sound
        trainMediaPlayer = MediaPlayer.create(this, R.raw.trainfade)
        trainMediaPlayer.isLooping = false
        trainMediaPlayer.setOnCompletionListener {
            Handler(Looper.getMainLooper()).postDelayed({
                trainMediaPlayer.start()
            }, 3700) // 1 second delay
        }
        trainMediaPlayer.setVolume(0.6f, 0.6f)
        trainMediaPlayer.start()

        binding.apply {
            box1.startAnimation(neonPulse)
            box2.startAnimation(neonPulse)
            box3.startAnimation(neonPulse)
            box4.startAnimation(neonPulse)
            box5.startAnimation(neonPulse)
            box6.startAnimation(neonPulse)
            box7.startAnimation(neonPulse)
            box8.startAnimation(neonPulse)
            box9.startAnimation(neonPulse)

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
            toggleRainButton.setOnClickListener { toggleRainGif() }
//            muteButton.setOnClickListener { mute() }

            rainGif.visibility = View.VISIBLE // Set rain GIF visibility to VISIBLE by default
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        trainMediaPlayer.stop()
        rainMediaPlayer.stop()
    }

    private fun onButtonClick(button: Button) {
        if (button.text == "" && player == "p1") {
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
            button.startAnimation(rotateAnimation)

            button.background = ContextCompat.getDrawable(this, R.drawable.cross)
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

            val blockMove = findBlockingMove()
            val winMove = findWinningMove()
            if (winMove != null) {
                val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
                winMove.startAnimation(rotateAnimation)

                winMove.background = ContextCompat.getDrawable(this, R.drawable.o)
                winMove.text = "o"
                winMove.isClickable = false
            } else if (blockMove != null) {
                val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
                blockMove.startAnimation(rotateAnimation)

                blockMove.background = ContextCompat.getDrawable(this, R.drawable.o)
                blockMove.text = "o"
                blockMove.isClickable = false
            } else if (availableButtons.isNotEmpty()) {
                val randomButton = availableButtons.random()
                val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
                randomButton.startAnimation(rotateAnimation)

                randomButton.background = ContextCompat.getDrawable(this, R.drawable.o)
                randomButton.text = "o"
                randomButton.isClickable = false
            }
            player = "p1"
            win()
        }, 500)
    }

    private fun findBlockingMove(): Button? {
        binding.apply {
            if (box1.text == "x" && box2.text == "x" && box3.text == "") return box3
            if (box1.text == "x" && box2.text == "" && box3.text == "x") return box2
            if (box1.text == "" && box2.text == "x" && box3.text == "x") return box1
            if (box4.text == "x" && box5.text == "x" && box6.text == "") return box6
            if (box4.text == "x" && box5.text == "" && box6.text == "x") return box5
            if (box4.text == "" && box5.text == "x" && box6.text == "x") return box4
            if (box7.text == "x" && box8.text == "x" && box9.text == "") return box9
            if (box7.text == "x" && box8.text == "" && box9.text == "x") return box8
            if (box7.text == "" && box8.text == "x" && box9.text == "x") return box7
            if (box1.text == "x" && box4.text == "x" && box7.text == "") return box7
            if (box1.text == "x" && box4.text == "" && box7.text == "x") return box4
            if (box1.text == "" && box4.text == "x" && box7.text == "x") return box1
            if (box2.text == "x" && box5.text == "x" && box8.text == "") return box8
            if (box2.text == "x" && box5.text == "" && box8.text == "x") return box5
            if (box2.text == "" && box5.text == "x" && box8.text == "x") return box2
            if (box3.text == "x" && box6.text == "x" && box9.text == "") return box9
            if (box3.text == "x" && box6.text == "" && box9.text == "x") return box6
            if (box3.text == "" && box6.text == "x" && box9.text == "x") return box3
            if (box1.text == "x" && box5.text == "x" && box9.text == "") return box9
            if (box1.text == "x" && box5.text == "" && box9.text == "x") return box5
            if (box1.text == "" && box5.text == "x" && box9.text == "x") return box1
            if (box3.text == "x" && box5.text == "x" && box7.text == "") return box7
            if (box3.text == "x" && box5.text == "" && box7.text == "x") return box5
            if (box3.text == "" && box5.text == "x" && box7.text == "x") return box3
        }
        return null
    }

    private fun findWinningMove(): Button? {
        binding.apply {
            if (box1.text == "o" && box2.text == "o" && box3.text == "") return box3
            if (box1.text == "o" && box2.text == "" && box3.text == "o") return box2
            if (box1.text == "" && box2.text == "o" && box3.text == "o") return box1
            if (box4.text == "o" && box5.text == "o" && box6.text == "") return box6
            if (box4.text == "o" && box5.text == "" && box6.text == "o") return box5
            if (box4.text == "" && box5.text == "o" && box6.text == "o") return box4
            if (box7.text == "o" && box8.text == "o" && box9.text == "") return box9
            if (box7.text == "o" && box8.text == "" && box9.text == "o") return box8
            if (box7.text == "" && box8.text == "o" && box9.text == "o") return box7
            if (box1.text == "o" && box4.text == "o" && box7.text == "") return box7
            if (box1.text == "o" && box4.text == "" && box7.text == "o") return box4
            if (box1.text == "" && box4.text == "o" && box7.text == "o") return box1
            if (box2.text == "o" && box5.text == "o" && box8.text == "") return box8
            if (box2.text == "o" && box5.text == "" && box8.text == "o") return box5
            if (box2.text == "" && box5.text == "o" && box8.text == "o") return box2
            if (box3.text == "o" && box6.text == "o" && box9.text == "") return box9
            if (box3.text == "o" && box6.text == "" && box9.text == "o") return box6
            if (box3.text == "" && box6.text == "o" && box9.text == "o") return box3
            if (box1.text == "o" && box5.text == "o" && box9.text == "") return box9
            if (box1.text == "o" && box5.text == "" && box9.text == "o") return box5
            if (box1.text == "" && box5.text == "o" && box9.text == "o") return box1
            if (box3.text == "o" && box5.text == "o" && box7.text == "") return box7
            if (box3.text == "o" && box5.text == "" && box7.text == "o") return box5
            if (box3.text == "" && box5.text == "o" && box7.text == "o") return box3
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
                disableButtons()
            } else if (
                box1.text != "" && box2.text != "" && box3.text != "" &&
                box4.text != "" && box5.text != "" && box6.text != "" &&
                box7.text != "" && box8.text != "" && box9.text != ""
            ) {
                tvResult.text = "Match Draw"
                disableButtons()
                resetButton.visibility = View.VISIBLE
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
            box1.setBackgroundResource(R.drawable.box)
            box2.setBackgroundResource(R.drawable.box)
            box3.setBackgroundResource(R.drawable.box)
            box4.setBackgroundResource(R.drawable.box)
            box5.setBackgroundResource(R.drawable.box)
            box6.setBackgroundResource(R.drawable.box)
            box7.setBackgroundResource(R.drawable.box)
            box8.setBackgroundResource(R.drawable.box)
            box9.setBackgroundResource(R.drawable.box)

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

//    private fun mute() {
//        if (isMuted) {
//            // Unmute all sounds
//            rainMediaPlayer.setVolume(0.5f, 0.5f)
//            trainMediaPlayer.setVolume(0.8f, 0.8f)
//            MediaPlayerSingleton.musicMediaPlayer?.setVolume(1.0f, 1.0f)
//        } else {
//            // Mute all sounds
//            rainMediaPlayer.setVolume(0.0f, 0.0f)
//            trainMediaPlayer.setVolume(0.0f, 0.0f)
//            MediaPlayerSingleton.musicMediaPlayer?.setVolume(0.0f, 0.0f)
//        }
//        isMuted = !isMuted
//    }

    private fun toggleRainGif() {
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
        clickMediaPlayer.start()
        clickMediaPlayer.setVolume(0.5f, 0.5f)
        clickMediaPlayer.setOnCompletionListener {
            it.release()
        }
        if (isRainGifVisible) {
            binding.rainGif.visibility = View.GONE
            rainMediaPlayer.pause()
        } else {
            binding.rainGif.visibility = View.VISIBLE
            rainMediaPlayer.start()
            if (!isMuted) {
                fadeInRainSound()
            } else {
                rainMediaPlayer.setVolume(0.0f, 0.0f)
            }
        }
        isRainGifVisible = !isRainGifVisible
    }

    private fun fadeInRainSound() {
        val fadeDuration = 500 // 4 seconds
        val fadeSteps = 5
        val stepDuration = fadeDuration / fadeSteps
        val volumeStep = 1.0f / fadeSteps

        var currentVolume = 0.0f
        rainMediaPlayer.setVolume(currentVolume, currentVolume)

        val handler = Handler(Looper.getMainLooper())
        for (i in 1..fadeSteps) {
            handler.postDelayed({
                currentVolume += volumeStep
                rainMediaPlayer.setVolume(currentVolume, currentVolume)
            }, (i * stepDuration).toLong())
        }
    }
    override fun onPause() {
        super.onPause()
        if (rainMediaPlayer.isPlaying) {
            rainMediaPlayer.pause()
        }
        if (trainMediaPlayer.isPlaying) {
            trainMediaPlayer.pause()
        }
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (!rainMediaPlayer.isPlaying) {
            rainMediaPlayer.start()
        }
        if (!trainMediaPlayer.isPlaying) {
            trainMediaPlayer.start()
        }
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

}