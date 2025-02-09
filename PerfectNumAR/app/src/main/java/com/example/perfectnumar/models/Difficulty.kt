package com.example.perfectnumar.models

enum class Difficulty {
    EASY, MEDIUM, HARD;


    fun getTimeLimit(): Int = when (this) {
        EASY -> 0
        MEDIUM -> 90
        HARD -> 45
    }

    fun getDisplayName(): String = when (this) {
        EASY -> "Kolay"
        MEDIUM -> "Orta"
        HARD -> "Zor"
    }
}