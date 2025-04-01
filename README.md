# Tic Tac Toe Game - Comprehensive Documentation

## Introduction

The Tic Tac Toe game is an Android application developed in Kotlin that offers a fun and engaging experience for players. The game features two distinct visual styles: a Basic minimalist theme and a Retro animated theme with glowing neon effects. The core game logic is shared efficiently across these themes through the use of inheritance and polymorphism, ensuring clean, maintainable code.

### Key Features

- **Dual UI Themes**: Basic (minimalist) and Retro (animated) versions
- **Smart AI Opponent**: Three-level decision making (win/block/random)
- **Rich Media Experience**: Background music and sound effects
- **Responsive Design**: Adapts to different screen sizes
- **Efficient Resource Management**: Optimized animations and audio

## Setup

### Repository Content

```
TicTacToe/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/tictactoe/
│   │   │   │   ├── BaseTicTacToeActivity.kt  // Centralizing shared game logic
│   │   │   │   ├── BasicActivity.kt          // Basic game class
│   │   │   │   ├── RetroActivity.kt          // Retro Game class
│   │   │   │   ├── MainActivity.kt           // Main page class
│   │   │   │   ├── MediaPlayerSingleton.kt   // Manages shared media player
│   │   │   │   └── TypeWriterAnimation       // Animates text like a typewriter
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │    ├── activity_basic.xml
│   │   │       │    ├── activity_retro.xml
│   │   │       │    └── activity_main.xml
│   │   │       ├── drawable/
│   │   │       ├── anim/
│   │   │       └── raw/
│   │   │
├── Apk Game/
│   └── TicTacToe.apk
├── build.gradle
└── settings.gradle
```

### How to Run the Code

1. **Requirements**:
   - Android Studio (latest version)
   - Android SDK (API level 28+)
   - Java 8 or Kotlin plugin

2. **Setup**:
   ```bash
   git clone [https://github.com/your-repo/tictactoe.git](https://github.com/BassemMagdi0007/Tic-Tac-Toe.git)
   cd tictactoe
   ```
   
3. **Run**:
   - Open project in Android Studio
   - Sync Gradle dependencies
   - Run on emulator or physical device

4. **Install Application (Optional):**
   - Open folder `Apk Game/`
   - Double click and install `TicTacToe.apk` <br >

**NOTE:** Since this is an indie game (App) that is not published on the App Store, a security message will pop up stating app is not trusted or not secured, well... it is secured :] 

### Used Libraries

- **Android SDK**: Core framework components
- **Glide**: For efficient GIF animation loading
- **AndroidX**: For modern UI components
- **Kotlin**: Primary programming language

## Code Structure

### 1. Base Activity (Core Game Logic)
```kotlin
abstract class BaseTicTacToeActivity : AppCompatActivity() {
    protected var player = "p1" // Current player
    
    // Abstract methods for UI implementations
    protected abstract fun getButtonBackgroundDrawable(isX: Boolean): Int
    
    // Shared game logic
    protected fun onButtonClick(button: Button) {
        // Handle player moves
    }
    
    protected fun cpuTurn() {
        // AI decision making
    }
}
```

### 2. Basic Activity (Simple UI)
```kotlin
class BasicActivity : BaseTicTacToeActivity() {
    override fun getButtonBackgroundDrawable(isX: Boolean): Int {
        return if (isX) R.drawable.cross_simple else R.drawable.circle_simple
    }
    // Basic UI implementations...
}
```

### 3. Retro Activity (Advanced UI)
```kotlin
class RetroActivity : BaseTicTacToeActivity() {
    private lateinit var rainMediaPlayer: MediaPlayer
    
    override fun getButtonBackgroundDrawable(isX: Boolean): Int {
        return if (isX) R.drawable.cross_neon else R.drawable.circle_neon
    }
    // Retro animations and sounds...
}
```

### 4. Media Management
```kotlin
object MediaPlayerSingleton {
    var musicMediaPlayer: MediaPlayer? = null
    
    fun init(context: Context) {
        musicMediaPlayer = MediaPlayer.create(context, R.raw.background_music)
    }
}
```

## Self Evaluation and Design Decisions

### 1. Architecture Evolution

**Initial Approach**:
- Single Activity with all logic
- Duplicated code for different themes
- Hardcoded AI behavior

**Problems Identified**:
- Code duplication
- Difficult to maintain
- Inflexible for new features

**Final Solution**:
- Base class with shared logic
- Child classes for specific UIs
- Configurable AI difficulty

### 2. AI Implementation Choices

**First Attempt**:
- Purely random moves
- Easy to beat
- No strategic depth

**Improved Version**:
```kotlin
private fun findWinningMove(): Button? {
    // Check all possible winning combinations
    if (box1.text == "o" && box2.text == "o" && box3.text == "") 
        return box3
    // Additional checks...
}
```

**Final Algorithm**:
1. Check for immediate win
2. Block opponent's win
3. Take center if available
4. Choose random corner
5. Default to random move

### 3. Performance Optimization

**Challenges**:
- Memory leaks with MediaPlayer
- GIF animation performance
- UI responsiveness during AI moves

**Solutions Implemented**:
- Singleton for music management
- Glide for efficient GIF handling
- Handler.postDelayed for AI moves

```kotlin
Handler(Looper.getMainLooper()).postDelayed({
    // AI move logic
}, 500) // 500ms delay for smooth UX
```

### 4. Lessons Learned

- **Inheritance** is powerful but requires careful planning
- **Resource management** is crucial in mobile apps
- **UX polish** (animations/sounds) significantly improves engagement
- **Testing** across devices reveals performance issues

## Output Format

The game provides visual feedback through:

1. **Game Board Updates**:
   - X/O marks appear on button clicks
   - Button states update (enabled/disabled)

2. **Game Status Messages**:
```kotlin
tv_result.text = when {
    isWin("x") -> "You Won!"
    isWin("o") -> "Computer Wins!"
    isDraw() -> "Match Draw"
    else -> ""
}
```

3. **Visual Feedback**:
   - Winning line highlight (Retro version)
   - Button animations
   - Background effects

4. **Audio Feedback**:
   - Click sounds
   - Win/lose sound effects
   - Background music

Example game flow:
1. Player taps a square (X appears with sound)
2. AI makes move after brief delay (O appears)
3. Game continues until win/draw
4. Reset button appears to restart game
