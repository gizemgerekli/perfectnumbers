package com.example.perfectnumar.models

data class GameState(
    val currentNumber: Int = 0,

    val selectedFactors: List<Int> = emptyList(),
    val timeRemaining: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
    val isGameActive: Boolean = false,
    val currentInput: String = "",
    val factorsSum: Int = 0
)