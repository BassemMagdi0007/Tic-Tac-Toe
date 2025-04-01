package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

/**
 * Abstract base class for Tic Tac Toe game activities.
 * Contains all shared game logic between different game versions (Basic/Retro).
 * Child classes must implement UI-specific methods.
 */
abstract class BaseTicTacToeActivity : AppCompatActivity() {
    // Tracks current player turn ("p1" for human player, "cpu" for computer)
    protected var player = "p1"

    //----------------------------------------------------------------------------------------------
    // ABSTRACT METHODS (Must be implemented by child classes)
    //----------------------------------------------------------------------------------------------

    /**
     * Provides the appropriate drawable resource for X or O marks
     * @param isX True for X mark, false for O mark
     * @return Drawable resource ID for the specified mark
     */
    protected abstract fun getButtonBackgroundDrawable(isX: Boolean): Int

    /**
     * Provides the drawable resource for empty game boxes
     * @return Drawable resource ID for empty boxes
     */
    protected abstract fun getBoxBackgroundDrawable(): Int

    //----------------------------------------------------------------------------------------------
    // CORE GAME LOGIC
    //----------------------------------------------------------------------------------------------

    /**
     * Handles player moves on game board buttons
     * @param button The clicked game button
     */
    protected fun onButtonClick(button: Button) {
        // Only process if button is empty and it's player's turn
        if (button.text == "" && player == "p1") {
            // Set X mark and disable button
            button.background = ContextCompat.getDrawable(this, getButtonBackgroundDrawable(true))
            button.text = "x"
            button.isClickable = false

            // Check for win condition
            win()

            // If game continues, switch to CPU turn
            if (!isGameWon()) {
                player = "cpu"
                cpuTurn()
            }
        }
    }

    /**
     * Executes computer's turn with AI logic after short delay
     */
    protected fun cpuTurn() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Get list of available (empty) buttons
            val availableButtons = getGameButtons().filter { it.text == "" }

            // Try to find winning move first
            val winMove = findWinningMove()
            if (winMove != null) {
                // Place O in winning position
                winMove.background = ContextCompat.getDrawable(this, getButtonBackgroundDrawable(false))
                winMove.text = "o"
                winMove.isClickable = false
            } else {
                // If no winning move, try to block player
                val blockMove = findBlockingMove()
                if (blockMove != null) {
                    // Place O to block player's potential win
                    blockMove.background = ContextCompat.getDrawable(this, getButtonBackgroundDrawable(false))
                    blockMove.text = "o"
                    blockMove.isClickable = false
                } else if (availableButtons.isNotEmpty()) {
                    // If no strategic moves, choose random available spot
                    val randomButton = availableButtons.random()
                    randomButton.background = ContextCompat.getDrawable(this, getButtonBackgroundDrawable(false))
                    randomButton.text = "o"
                    randomButton.isClickable = false
                }
            }

            // Switch back to player turn and check game state
            player = "p1"
            win()
        }, 500) // 500ms delay for better UX
    }

    //----------------------------------------------------------------------------------------------
    // AI LOGIC METHODS
    //----------------------------------------------------------------------------------------------

    /**
     * Finds a winning move for computer (O)
     * @return Button representing winning move, or null if none exists
     */
    protected fun findWinningMove(): Button? {
        val buttons = getGameButtons()
        // Check all possible winning combinations for computer (O)

        // Horizontal checks
        if (buttons[0].text == "o" && buttons[1].text == "o" && buttons[2].text == "") return buttons[2]
        if (buttons[0].text == "o" && buttons[1].text == "" && buttons[2].text == "o") return buttons[1]
        if (buttons[0].text == "" && buttons[1].text == "o" && buttons[2].text == "o") return buttons[0]
        if (buttons[3].text == "o" && buttons[4].text == "o" && buttons[5].text == "") return buttons[5]
        if (buttons[3].text == "o" && buttons[4].text == "" && buttons[5].text == "o") return buttons[4]
        if (buttons[3].text == "" && buttons[4].text == "o" && buttons[5].text == "o") return buttons[3]
        if (buttons[6].text == "o" && buttons[7].text == "o" && buttons[8].text == "") return buttons[8]
        if (buttons[6].text == "o" && buttons[7].text == "" && buttons[8].text == "o") return buttons[7]
        if (buttons[6].text == "" && buttons[7].text == "o" && buttons[8].text == "o") return buttons[6]

        // Vertical checks
        if (buttons[0].text == "o" && buttons[3].text == "o" && buttons[6].text == "") return buttons[6]
        if (buttons[0].text == "o" && buttons[3].text == "" && buttons[6].text == "o") return buttons[3]
        if (buttons[0].text == "" && buttons[3].text == "o" && buttons[6].text == "o") return buttons[0]
        if (buttons[1].text == "o" && buttons[4].text == "o" && buttons[7].text == "") return buttons[7]
        if (buttons[1].text == "o" && buttons[4].text == "" && buttons[7].text == "o") return buttons[4]
        if (buttons[1].text == "" && buttons[4].text == "o" && buttons[7].text == "o") return buttons[1]
        if (buttons[2].text == "o" && buttons[5].text == "o" && buttons[8].text == "") return buttons[8]
        if (buttons[2].text == "o" && buttons[5].text == "" && buttons[8].text == "o") return buttons[5]
        if (buttons[2].text == "" && buttons[5].text == "o" && buttons[8].text == "o") return buttons[2]

        // Diagonal checks
        if (buttons[0].text == "o" && buttons[4].text == "o" && buttons[8].text == "") return buttons[8]
        if (buttons[0].text == "o" && buttons[4].text == "" && buttons[8].text == "o") return buttons[4]
        if (buttons[0].text == "" && buttons[4].text == "o" && buttons[8].text == "o") return buttons[0]
        if (buttons[2].text == "o" && buttons[4].text == "o" && buttons[6].text == "") return buttons[6]
        if (buttons[2].text == "o" && buttons[4].text == "" && buttons[6].text == "o") return buttons[4]
        if (buttons[2].text == "" && buttons[4].text == "o" && buttons[6].text == "o") return buttons[2]

        return null // No winning move found
    }

    /**
     * Finds a move to block player's potential win (X)
     * @return Button representing blocking move, or null if none exists
     */
    protected fun findBlockingMove(): Button? {
        val buttons = getGameButtons()
        // Check all possible winning combinations for player (X) to block

        // [Same pattern as findWinningMove but checking for X instead of O]
        // Horizontal checks
        if (buttons[0].text == "x" && buttons[1].text == "x" && buttons[2].text == "") return buttons[2]
        if (buttons[0].text == "x" && buttons[1].text == "" && buttons[2].text == "x") return buttons[1]
        if (buttons[0].text == "" && buttons[1].text == "x" && buttons[2].text == "x") return buttons[0]
        if (buttons[3].text == "x" && buttons[4].text == "x" && buttons[5].text == "") return buttons[5]
        if (buttons[3].text == "x" && buttons[4].text == "" && buttons[5].text == "x") return buttons[4]
        if (buttons[3].text == "" && buttons[4].text == "x" && buttons[5].text == "x") return buttons[3]
        if (buttons[6].text == "x" && buttons[7].text == "x" && buttons[8].text == "") return buttons[8]
        if (buttons[6].text == "x" && buttons[7].text == "" && buttons[8].text == "x") return buttons[7]
        if (buttons[6].text == "" && buttons[7].text == "x" && buttons[8].text == "x") return buttons[6]

        // Vertical checks
        if (buttons[0].text == "x" && buttons[3].text == "x" && buttons[6].text == "") return buttons[6]
        if (buttons[0].text == "x" && buttons[3].text == "" && buttons[6].text == "x") return buttons[3]
        if (buttons[0].text == "" && buttons[3].text == "x" && buttons[6].text == "x") return buttons[0]
        if (buttons[1].text == "x" && buttons[4].text == "x" && buttons[7].text == "") return buttons[7]
        if (buttons[1].text == "x" && buttons[4].text == "" && buttons[7].text == "x") return buttons[4]
        if (buttons[1].text == "" && buttons[4].text == "x" && buttons[7].text == "x") return buttons[1]
        if (buttons[2].text == "x" && buttons[5].text == "x" && buttons[8].text == "") return buttons[8]
        if (buttons[2].text == "x" && buttons[5].text == "" && buttons[8].text == "x") return buttons[5]
        if (buttons[2].text == "" && buttons[5].text == "x" && buttons[8].text == "x") return buttons[2]

        // Diagonal checks
        if (buttons[0].text == "x" && buttons[4].text == "x" && buttons[8].text == "") return buttons[8]
        if (buttons[0].text == "x" && buttons[4].text == "" && buttons[8].text == "x") return buttons[4]
        if (buttons[0].text == "" && buttons[4].text == "x" && buttons[8].text == "x") return buttons[0]
        if (buttons[2].text == "x" && buttons[4].text == "x" && buttons[6].text == "") return buttons[6]
        if (buttons[2].text == "x" && buttons[4].text == "" && buttons[6].text == "x") return buttons[4]
        if (buttons[2].text == "" && buttons[4].text == "x" && buttons[6].text == "x") return buttons[2]

        return null // No blocking move needed
    }

    /**
     * Checks if player (X) has won the game
     * @return True if player has won, false otherwise
     */
    protected fun isGameWon(): Boolean {
        val buttons = getGameButtons()
        // Check all possible winning combinations for player (X)
        return ( // Horizontal wins
                (buttons[0].text == "x" && buttons[1].text == "x" && buttons[2].text == "x") ||
                        (buttons[3].text == "x" && buttons[4].text == "x" && buttons[5].text == "x") ||
                        (buttons[6].text == "x" && buttons[7].text == "x" && buttons[8].text == "x") ||
                        // Vertical wins
                        (buttons[0].text == "x" && buttons[3].text == "x" && buttons[6].text == "x") ||
                        (buttons[1].text == "x" && buttons[4].text == "x" && buttons[7].text == "x") ||
                        (buttons[2].text == "x" && buttons[5].text == "x" && buttons[8].text == "x") ||
                        // Diagonal wins
                        (buttons[0].text == "x" && buttons[4].text == "x" && buttons[8].text == "x") ||
                        (buttons[2].text == "x" && buttons[4].text == "x" && buttons[6].text == "x")
                )
    }

    //----------------------------------------------------------------------------------------------
    // GAME STATE MANAGEMENT
    //----------------------------------------------------------------------------------------------

    /**
     * Checks and handles game ending conditions (win/loss/draw)
     */
    @SuppressLint("SetTextI18n")
    protected fun win() {
        val buttons = getGameButtons()

        // Check for player (X) win
        if ( // Horizontal wins
            (buttons[0].text == "x" && buttons[1].text == "x" && buttons[2].text == "x") ||
            (buttons[3].text == "x" && buttons[4].text == "x" && buttons[5].text == "x") ||
            (buttons[6].text == "x" && buttons[7].text == "x" && buttons[8].text == "x") ||
            // Vertical wins
            (buttons[0].text == "x" && buttons[3].text == "x" && buttons[6].text == "x") ||
            (buttons[1].text == "x" && buttons[4].text == "x" && buttons[7].text == "x") ||
            (buttons[2].text == "x" && buttons[5].text == "x" && buttons[8].text == "x") ||
            // Diagonal wins
            (buttons[0].text == "x" && buttons[4].text == "x" && buttons[8].text == "x") ||
            (buttons[2].text == "x" && buttons[4].text == "x" && buttons[6].text == "x")
        ) {
            setResultText("Won The GAME!")
            disableButtons()
        }
        // Check for computer (O) win
        else if ( // Horizontal wins
            (buttons[0].text == "o" && buttons[1].text == "o" && buttons[2].text == "o") ||
            (buttons[3].text == "o" && buttons[4].text == "o" && buttons[5].text == "o") ||
            (buttons[6].text == "o" && buttons[7].text == "o" && buttons[8].text == "o") ||
            // Vertical wins
            (buttons[0].text == "o" && buttons[3].text == "o" && buttons[6].text == "o") ||
            (buttons[1].text == "o" && buttons[4].text == "o" && buttons[7].text == "o") ||
            (buttons[2].text == "o" && buttons[5].text == "o" && buttons[8].text == "o") ||
            // Diagonal wins
            (buttons[0].text == "o" && buttons[4].text == "o" && buttons[8].text == "o") ||
            (buttons[2].text == "o" && buttons[4].text == "o" && buttons[6].text == "o")
        ) {
            setResultText("Lost The GAME!")
            disableButtons()
        }
        // Check for draw (all boxes filled with no winner)
        else if (
            buttons[0].text != "" && buttons[1].text != "" && buttons[2].text != "" &&
            buttons[3].text != "" && buttons[4].text != "" && buttons[5].text != "" &&
            buttons[6].text != "" && buttons[7].text != "" && buttons[8].text != ""
        ) {
            setResultText("Match Draw")
            disableButtons()
            showResetButton()
        }
    }

    /**
     * Disables all game buttons when game ends
     */
    protected fun disableButtons() {
        getGameButtons().forEach { it.isClickable = false }
        showResetButton()
    }

    /**
     * Resets game to initial state
     */
    protected fun resetGame() {
        player = "p1" // Reset to player's turn
        getGameButtons().forEach {
            it.setBackgroundResource(getBoxBackgroundDrawable()) // Reset box appearance
            it.text = "" // Clear marks
            it.isClickable = true // Re-enable buttons
        }
        setResultText("") // Clear result message
        hideResetButton() // Hide reset button
    }

    //----------------------------------------------------------------------------------------------
    // REQUIRED IMPLEMENTATIONS (Child classes must provide these)
    //----------------------------------------------------------------------------------------------

    /**
     * Gets all game buttons in proper order (left-to-right, top-to-bottom)
     * @return List of all 9 game buttons
     */
    protected abstract fun getGameButtons(): List<Button>

    /**
     * Updates the game result text display
     * @param text The result message to display
     */
    protected abstract fun setResultText(text: String)

    /**
     * Shows the reset button (typically when game ends)
     */
    protected abstract fun showResetButton()

    /**
     * Hides the reset button (typically when new game starts)
     */
    protected abstract fun hideResetButton()
}