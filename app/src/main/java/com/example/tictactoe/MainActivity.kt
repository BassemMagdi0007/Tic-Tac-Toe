package com.example.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tictactoe.databinding.ActivityMainBinding
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var clickMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val neonPulse = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)

        Glide.with(this)
            .asGif()
            .load(R.drawable.main)
            .into(binding.backgroundGif)

        // Create an AnimationSet to combine fadeIn and neonPulse animations
        val buttonAnimationSet = AnimationSet(true).apply {
            addAnimation(fadeIn)
            addAnimation(neonPulse)
        }

        if (MediaPlayerSingleton.musicMediaPlayer == null) {
            MediaPlayerSingleton.musicMediaPlayer = MediaPlayer.create(this, R.raw.music)
            MediaPlayerSingleton.musicMediaPlayer?.isLooping = true
            MediaPlayerSingleton.musicMediaPlayer?.setVolume(0.5f, 0.5f)
            MediaPlayerSingleton.musicMediaPlayer?.start()
        }

        // Apply fade-in animation to the background and combined animation to the buttons after the layout is drawn
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.backgroundGif.startAnimation(fadeIn)
                binding.buttonBasic.startAnimation(buttonAnimationSet)
                binding.buttonRetro.startAnimation(buttonAnimationSet)
            }
        })

        binding.buttonBasic.setOnClickListener {
            clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
            clickMediaPlayer.start()
            clickMediaPlayer.setVolume(0.5f, 0.5f)
            clickMediaPlayer.setOnCompletionListener {
                it.release()
            }
            val intent = Intent(this, Basic::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        binding.buttonRetro.setOnClickListener {
            clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
            clickMediaPlayer.start()
            clickMediaPlayer.setVolume(0.5f, 0.5f)
            clickMediaPlayer.setOnCompletionListener {
                it.release()
            }
            val intent = Intent(this, Retro::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
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