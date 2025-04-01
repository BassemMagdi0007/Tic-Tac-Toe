package com.example.tictactoe

import android.animation.ValueAnimator
import android.widget.TextView

class TypeWriterAnimation(private val textView: TextView, private val text: String) {
    fun startAnimation(duration: Long) {
        val animator = ValueAnimator.ofInt(0, text.length)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            textView.text = text.substring(0, animatedValue)
        }
        animator.start()
    }
}