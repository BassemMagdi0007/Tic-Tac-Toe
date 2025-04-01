package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import com.example.tictactoe.databinding.ActivityBasicBinding

/**
 * Basic version of the Tic Tac Toe game that inherits from BaseTicTacToeActivity.
 * This class handles the basic/styled UI while the core game logic is inherited.
 */
class Basic : BaseTicTacToeActivity() {
    // View binding instance for accessing UI elements
    private lateinit var binding: ActivityBasicBinding

    //----------------------------------------------------------------------------------------------
    /**
     * Called when the activity is first created.
     * Sets up the UI, initializes game state, and configures click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable edge-to-edge display (for modern Android UI)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize view binding and set content view
        binding = ActivityBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially hide the reset button (will show when game ends)
        binding.resetButton.visibility = View.GONE

        // Access the shared MediaPlayer instance for background music
        val musicMediaPlayer = MediaPlayerSingleton.musicMediaPlayer
        musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start() // Start music if not already playing
            }
        }

        // Set up click listeners for all game buttons and reset button
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
            resetButton.setOnClickListener { resetGame() } // Inherited reset functionality
        }
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Handles the back button press with a fade transition animation.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        // Apply custom fade animations when going back to the MainActivity
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Provides the appropriate drawable resource for X or O marks.
     * @param isX Boolean indicating whether the mark is X or O
     * @return The drawable resource ID for the specified mark
     */
    override fun getButtonBackgroundDrawable(isX: Boolean): Int {
        return if (isX) R.drawable.cross2 else R.drawable.o2
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Provides the drawable resource for empty game boxes.
     * @return The drawable resource ID for empty boxes
     */
    override fun getBoxBackgroundDrawable(): Int {
        return R.drawable.box2
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Gets a list of all game buttons in the proper order (left-to-right, top-to-bottom).
     * @return List of all 9 game buttons
     */
    override fun getGameButtons(): List<Button> {
        return listOf(
            binding.box1, binding.box2, binding.box3,  // First row
            binding.box4, binding.box5, binding.box6,  // Second row
            binding.box7, binding.box8, binding.box9   // Third row
        )
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Updates the game result text display.
     * @param text The result text to display (win/loss/draw message)
     */
    override fun setResultText(text: String) {
        binding.tvResult.text = text
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Shows the reset button (typically called when game ends).
     */
    override fun showResetButton() {
        binding.resetButton.visibility = View.VISIBLE
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Hides the reset button (typically called when starting new game).
     */
    override fun hideResetButton() {
        binding.resetButton.visibility = View.GONE
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Called when activity is paused. Pauses background music.
     */
    override fun onPause() {
        super.onPause()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (it.isPlaying) {
                it.pause() // Pause music when activity is not in foreground
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Called when activity resumes. Resumes background music if it was playing.
     */
    override fun onResume() {
        super.onResume()
        MediaPlayerSingleton.musicMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start() // Resume music when activity comes to foreground
            }
        }
    }
}