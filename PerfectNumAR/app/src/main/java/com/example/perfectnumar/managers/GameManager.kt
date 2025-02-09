package com.example.perfectnumar.managers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.perfectnumar.models.GameState
import com.example.perfectnumar.models.Difficulty
import com.example.perfectnumar.utils.NumberUtils
import com.example.perfectnumar.utils.Constants

class GameManager {
    private val _gameState = MutableLiveData(GameState())
    val gameState: LiveData<GameState> = _gameState

    private val timerManager = TimerManager { updateTimeRemaining(it) }

    fun startGame(difficulty: Difficulty) {
        val newNumber = NumberUtils.generateRandomNumber()
        _gameState.value = GameState(
            currentNumber = newNumber,
            difficulty = difficulty,
            isGameActive = true,
            timeRemaining = difficulty.getTimeLimit() // Başlangıç süresini ayarla
        )

        // EASY modunda veya süre 0'sa timer'ı başlatma
        if (difficulty != Difficulty.EASY && difficulty.getTimeLimit() > 0) {
            timerManager.startTimer(difficulty.getTimeLimit())
        }
    }

    fun addInput(number: Int) {
        val currentState = _gameState.value ?: return
        // Oyun aktif değilse veya maksimum uzunluğa ulaşıldıysa işlem yapma
        if (!currentState.isGameActive ||
            currentState.currentInput.length >= Constants.MAX_INPUT_LENGTH) return

        val newInput = currentState.currentInput + number.toString()
        _gameState.value = currentState.copy(currentInput = newInput)
    }

    fun confirmInput() {
        val currentState = _gameState.value ?: return
        // Oyun aktif değilse işlem yapma
        if (!currentState.isGameActive) return

        val input = currentState.currentInput.toIntOrNull() ?: return

        if (NumberUtils.isValidFactor(input, currentState.currentNumber)) {
            val newFactors = currentState.selectedFactors + input
            val newSum = NumberUtils.calculateFactorsSum(newFactors)
            _gameState.value = currentState.copy(
                selectedFactors = newFactors,
                factorsSum = newSum,
                currentInput = ""
            )
        }
    }

    fun clearInput() {
        val currentState = _gameState.value ?: return
        if (!currentState.isGameActive) return
        _gameState.value = currentState.copy(currentInput = "")
    }

    fun checkAnswer(isPerfectGuess: Boolean): Boolean {
        val currentState = _gameState.value ?: return false
        if (!currentState.isGameActive) return false
        return isPerfectGuess == NumberUtils.isPerfectNumber(currentState.currentNumber)
    }

    private fun updateTimeRemaining(timeRemaining: Int) {
        val currentState = _gameState.value ?: return
        _gameState.value = currentState.copy(
            timeRemaining = timeRemaining,
            // Süre bittiğinde oyunu sonlandır (EASY mod hariç)
            isGameActive = timeRemaining > 0 || currentState.difficulty == Difficulty.EASY
        )
    }

    fun endGame() {
        timerManager.stopTimer()
        _gameState.value = GameState()
    }
}