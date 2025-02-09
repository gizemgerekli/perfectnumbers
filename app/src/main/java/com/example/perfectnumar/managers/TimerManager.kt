package com.example.perfectnumar.managers

import android.os.CountDownTimer


class TimerManager(private val onTick: (Int) -> Unit) {
    private var timer: CountDownTimer? = null

    fun startTimer(seconds: Int) {
        timer?.cancel()
        
        if (seconds <= 0) return

        timer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                onTick(0)
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}