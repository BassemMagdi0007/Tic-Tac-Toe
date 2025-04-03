package com.example.tictactoe

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tictactoe.databinding.ActivityRetroBinding

/**
 * Retro-styled version of Tic Tac Toe that inherits from BaseTicTacToeActivity.
 * Features special visual effects, animations, and sound atmosphere.
 */
class Retro : BaseTicTacToeActivity() {
    // View binding instance for accessing UI elements
    private lateinit var binding: ActivityRetroBinding

    // State variables for visual effects
    private var isRainGifVisible = true
//    private var isMuted = false

    // Media players for sound effects
    private lateinit var rainMediaPlayer: MediaPlayer
    private lateinit var trainMediaPlayer: MediaPlayer
    private lateinit var clickMediaPlayer: MediaPlayer

    //----------------------------------------------------------------------------------------------
    /**
     * Initializes the retro-styled game activity with special effects and animations.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()
        setContentView(R.layout.activity_retro)

        // Initialize click sound effect
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)

        // Clear Glide cache to ensure fresh loading of GIFs
        Glide.get(this).clearMemory()
        Thread {
            Glide.get(this).clearDiskCache()
        }.start()

        // Initialize view binding
        binding = ActivityRetroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //------------------------------------------------------------------------------------------
        // VISUAL EFFECTS SETUP
        //------------------------------------------------------------------------------------------

        // Preload night background GIF for smooth display
        Glide.with(this)
            .asGif()
            .load(R.drawable.nights)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .preload()

        // Set up typewriter animation for the app title
        val typeWriterAnimation = TypeWriterAnimation(binding.appName, "Tic Tac Toe")
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                typeWriterAnimation.startAnimation(3000)  // 3 second animation
                handler.postDelayed(this, 7000)  // Repeat every 7 seconds
            }
        }
        handler.post(runnable)

        // Initially hide reset button
        binding.resetButton.visibility = View.GONE

        // Configure Glide options for GIF loading
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)

        // Load and display background GIF
        Glide.with(this)
            .asGif()
            .load(R.drawable.nights)
            .into(binding.backgroundGif)

        // Create neon pulse animation for game buttons
        val neonPulse = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)

        //------------------------------------------------------------------------------------------
        // SOUND EFFECTS SETUP
        //------------------------------------------------------------------------------------------

        // Start background music from singleton
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }

        // Load rain GIF with slight delay
        Handler(Looper.getMainLooper()).postDelayed({
            Glide.with(this)
                .asGif()
                .load(R.drawable.rain2)
                .apply(requestOptions)
                .into(binding.rainGif)
        }, 1000)

        // Initialize rain sound effect with loop behavior
        rainMediaPlayer = MediaPlayer.create(this, R.raw.rain)
        rainMediaPlayer.isLooping = false
        rainMediaPlayer.setOnCompletionListener {
            Handler(Looper.getMainLooper()).postDelayed({
                rainMediaPlayer.start()  // Restart after 1 second delay
            }, 1000)
        }
        rainMediaPlayer.setVolume(0.5f, 0.5f)
        rainMediaPlayer.start()

        // Initialize train sound effect with loop behavior
        trainMediaPlayer = MediaPlayer.create(this, R.raw.trainfade)
        trainMediaPlayer.isLooping = false
        trainMediaPlayer.setOnCompletionListener {
            Handler(Looper.getMainLooper()).postDelayed({
                trainMediaPlayer.start()  // Restart after 3.7 second delay
            }, 1000)
        }
        trainMediaPlayer.setVolume(0.6f, 0.6f)
        trainMediaPlayer.start()

        //------------------------------------------------------------------------------------------
        // GAME BOARD SETUP
        //------------------------------------------------------------------------------------------

        // Apply neon pulse animation to all game buttons
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

            // Set click listeners with additional animation effects
            box1.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box1.startAnimation(rotateAnimation)
                onButtonClick(box1)  // Inherited game logic
            }
            box2.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box2.startAnimation(rotateAnimation)
                onButtonClick(box2)  // Inherited game logic
            }
            box3.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box3.startAnimation(rotateAnimation)
                onButtonClick(box3)  // Inherited game logic
            }
            box4.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box4.startAnimation(rotateAnimation)
                onButtonClick(box4)  // Inherited game logic
            }
            box5.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box5.startAnimation(rotateAnimation)
                onButtonClick(box5)  // Inherited game logic
            }
            box6.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box6.startAnimation(rotateAnimation)
                onButtonClick(box6)  // Inherited game logic
            }
            box7.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box7.startAnimation(rotateAnimation)
                onButtonClick(box7)  // Inherited game logic
            }
            box8.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box8.startAnimation(rotateAnimation)
                onButtonClick(box8)  // Inherited game logic
            }
            box9.setOnClickListener {
                val rotateAnimation = AnimationUtils.loadAnimation(this@Retro, R.anim.pulse_glow)
                box9.startAnimation(rotateAnimation)
                onButtonClick(box9)  // Inherited game logic
            }

            resetButton.setOnClickListener { resetGame() }  // Inherited reset functionality
            toggleRainButton.setOnClickListener { toggleRainGif() }

            // Make rain GIF visible by default
            rainGif.visibility = View.VISIBLE
        }
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Handles back button press with fade transition and stops sound effects.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        trainMediaPlayer.stop()  // Stop train sound
        rainMediaPlayer.stop()   // Stop rain sound
    }

    //----------------------------------------------------------------------------------------------
    // ABSTRACT METHOD IMPLEMENTATIONS
    //----------------------------------------------------------------------------------------------

    /**
     * Provides retro-styled X or O drawable resources.
     * @param isX True for X mark, false for O mark
     * @return Drawable resource ID
     */
    override fun getButtonBackgroundDrawable(isX: Boolean): Int {
        return if (isX) R.drawable.cross else R.drawable.o
    }

    /**
     * Provides retro-styled empty box drawable.
     * @return Drawable resource ID for empty boxes
     */
    override fun getBoxBackgroundDrawable(): Int {
        return R.drawable.box
    }

    /**
     * Gets all game buttons in proper order.
     * @return List of all 9 game buttons
     */
    override fun getGameButtons(): List<Button> {
        return listOf(
            binding.box1, binding.box2, binding.box3,  // First row
            binding.box4, binding.box5, binding.box6,  // Second row
            binding.box7, binding.box8, binding.box9   // Third row
        )
    }

    /**
     * Updates the game result text display.
     * @param text The result message to display
     */
    override fun setResultText(text: String) {
        binding.tvResult.text = text
    }

    /**
     * Shows the reset button when game ends.
     */
    override fun showResetButton() {
        binding.resetButton.visibility = View.VISIBLE
    }

    /**
     * Hides the reset button when new game starts.
     */
    override fun hideResetButton() {
        binding.resetButton.visibility = View.GONE
    }

    //----------------------------------------------------------------------------------------------
    // RETRO-SPECIFIC FEATURES
    //----------------------------------------------------------------------------------------------

    /**
     * Toggles the rain visual effect and sound.
     */
    private fun toggleRainGif() {
        // Play click sound
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click)
        clickMediaPlayer.start()
        clickMediaPlayer.setVolume(0.5f, 0.5f)
        clickMediaPlayer.setOnCompletionListener {
            it.release()  // Release media player after completion
        }

        if (isRainGifVisible) {
            // Hide rain and pause sound
            binding.rainGif.visibility = View.GONE
            rainMediaPlayer.pause()
        } else {
            // Show rain and resume sound
            binding.rainGif.visibility = View.VISIBLE
            rainMediaPlayer.start()
//            if (!isMuted) {
//                fadeInRainSound()  // Smooth volume fade-in
//            } else {
//                rainMediaPlayer.setVolume(0.0f, 0.0f)  // Keep muted
//            }
        }
        isRainGifVisible = !isRainGifVisible  // Toggle state
    }

    /**
     * Gradually increases rain sound volume for smooth audio transition.
     */
    private fun fadeInRainSound() {
        val fadeDuration = 500    // 0.5 second fade duration
        val fadeSteps = 5         // Number of volume steps
        val stepDuration = fadeDuration / fadeSteps
        val volumeStep = 1.0f / fadeSteps  // Volume increment per step

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

    //----------------------------------------------------------------------------------------------
    // LIFECYCLE MANAGEMENT
    //----------------------------------------------------------------------------------------------

    /**
     * Pauses all media players when activity is not visible.
     */
    override fun onPause() {
        super.onPause()
        if (rainMediaPlayer.isPlaying) rainMediaPlayer.pause()
        if (trainMediaPlayer.isPlaying) trainMediaPlayer.pause()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (it.isPlaying) it.pause()
        }
    }

    /**
     * Resumes all media players when activity becomes visible.
     */
    override fun onResume() {
        super.onResume()
        if (!rainMediaPlayer.isPlaying) rainMediaPlayer.start()
        if (!trainMediaPlayer.isPlaying) trainMediaPlayer.start()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) it.start()
        }
    }
}
