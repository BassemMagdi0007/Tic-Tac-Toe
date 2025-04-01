package com.example.tictactoe

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tictactoe.databinding.ActivityMainBinding
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils

/**
 * Main entry point activity that serves as the game menu.
 * Features animated background and transition effects to game modes.
 */
class MainActivity : AppCompatActivity() {

    // View binding instance for accessing UI elements
    private lateinit var binding: ActivityMainBinding

    // MediaPlayer for button click sound effects
    private lateinit var clickMediaPlayer: MediaPlayer

    //----------------------------------------------------------------------------------------------
    // ACTIVITY LIFECYCLE
    //----------------------------------------------------------------------------------------------

    /**
     * Called when activity is first created.
     * Sets up UI elements, animations, and click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable edge-to-edge display
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize click sound effect
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)

        // Load animations from resources
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val neonPulse = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)

        // Load and display animated background GIF using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.main)
            .into(binding.backgroundGif)

        // Create combined animation for buttons (fade-in + pulse effect)
        val buttonAnimationSet = AnimationSet(true).apply {
            addAnimation(fadeIn)
            addAnimation(neonPulse)
        }

        // Initialize background music if not already playing
        if (MediaPlayerSingleton.musicMediaPlayer == null) {
            MediaPlayerSingleton.musicMediaPlayer = MediaPlayer.create(this, R.raw.music)
            MediaPlayerSingleton.musicMediaPlayer?.isLooping = true
            MediaPlayerSingleton.musicMediaPlayer?.setVolume(0.5f, 0.5f)  // Set to half volume
            MediaPlayerSingleton.musicMediaPlayer?.start()
        }

        //------------------------------------------------------------------------------------------
        // ANIMATION SETUP
        //------------------------------------------------------------------------------------------

        /**
         * Wait for layout to complete before starting animations
         * This ensures all views have proper dimensions before animating
         */
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove listener to avoid multiple calls
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Start animations on main elements
                binding.backgroundGif.startAnimation(fadeIn)
                binding.buttonBasic.startAnimation(buttonAnimationSet)
                binding.buttonRetro.startAnimation(buttonAnimationSet)
            }
        })

        //------------------------------------------------------------------------------------------
        // BUTTON CLICK HANDLERS
        //------------------------------------------------------------------------------------------

        // Basic game mode button
        binding.buttonBasic.setOnClickListener {
            // Play click sound with proper resource management
            clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
            clickMediaPlayer.start()
            clickMediaPlayer.setVolume(0.5f, 0.5f)
            clickMediaPlayer.setOnCompletionListener {
                it.release()  // Release MediaPlayer after sound completes
            }

            // Launch Basic game mode with fade transition
            val intent = Intent(this, Basic::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Retro game mode button
        binding.buttonRetro.setOnClickListener {
            // Play click sound with proper resource management
            clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
            clickMediaPlayer.start()
            clickMediaPlayer.setVolume(0.5f, 0.5f)
            clickMediaPlayer.setOnCompletionListener {
                it.release()  // Release MediaPlayer after sound completes
            }

            // Launch Retro game mode with fade transition
            val intent = Intent(this, Retro::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    //----------------------------------------------------------------------------------------------
    // ACTIVITY TRANSITIONS
    //----------------------------------------------------------------------------------------------

    /**
     * Handles back button press with fade transition effect.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //----------------------------------------------------------------------------------------------
    // MEDIA PLAYER MANAGEMENT
    //----------------------------------------------------------------------------------------------

    /**
     * Pauses background music when activity loses focus.
     */
    override fun onPause() {
        super.onPause()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    /**
     * Resumes background music when activity regains focus.
     */
    override fun onResume() {
        super.onResume()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }
}