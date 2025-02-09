package com.example.perfectnumar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.perfectnumar.ar.ARSceneManager
import com.example.perfectnumar.ar.ModelHelper
import com.example.perfectnumar.managers.GameManager
import com.example.perfectnumar.models.Difficulty
import com.google.ar.core.ArCoreApk
import com.google.ar.sceneform.ux.ArFragment

class MainActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private lateinit var arSceneManager: ARSceneManager
    private lateinit var gameManager: GameManager

    private lateinit var currentNumberText: TextView
    private lateinit var factorsText: TextView
    private lateinit var sumText: TextView
    private lateinit var timerText: TextView
    private lateinit var tempInputText: TextView
    private lateinit var confirmButton: Button
    private lateinit var clearButton: Button
    private lateinit var perfectButton: Button
    private lateinit var notPerfectButton: Button
    private lateinit var easyButton: Button
    private lateinit var mediumButton: Button
    private lateinit var hardButton: Button

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            initializeViews()
            checkPermissionAndInitialize()
        } catch (e: Exception) {
            Toast.makeText(this, "Başlatma hatası: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun checkPermissionAndInitialize() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            initializeAR()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun initializeAR() {
        try {
            arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
            setupGame()
            checkArCore()
        } catch (e: Exception) {
            Toast.makeText(this, "AR başlatılamadı: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkArCore() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            // Geçici durum, biraz bekle ve tekrar dene
            Handler(Looper.getMainLooper()).postDelayed({ checkArCore() }, 200)
            return
        }

        when (availability) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
                setupARScene()
            }
            ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD,
            ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
                try {
                    // AR Core yükleme isteği
                    val installStatus = ArCoreApk.getInstance().requestInstall(
                        this,
                        true, // Kullanıcıya sormak için true
                        ArCoreApk.InstallBehavior.REQUIRED,
                        ArCoreApk.UserMessageType.APPLICATION
                    )

                    when (installStatus) {
                        ArCoreApk.InstallStatus.INSTALLED -> {
                            setupARScene()
                        }
                        ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                            // Kullanıcı Play Store'a yönlendirildi
                            return
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "AR Core yüklenemedi: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                Toast.makeText(this, "Bu cihaz AR'ı desteklemiyor", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupARScene() {
        ModelHelper.loadModels(this) {
            arSceneManager = ARSceneManager(arFragment)
            arSceneManager.initialize()
            arSceneManager.setOnNumberTapListener { number ->
                gameManager.addInput(number)
            }
            observeGameState()
        }
    }

    private fun initializeViews() {
        currentNumberText = findViewById(R.id.currentNumberText)
        factorsText = findViewById(R.id.factorsText)
        sumText = findViewById(R.id.sumText)
        timerText = findViewById(R.id.timerText)
        tempInputText = findViewById(R.id.tempInputText)

        confirmButton = findViewById(R.id.confirmButton)
        clearButton = findViewById(R.id.clearButton)
        perfectButton = findViewById(R.id.perfectButton)
        notPerfectButton = findViewById(R.id.notPerfectButton)

        easyButton = findViewById(R.id.easyButton)
        mediumButton = findViewById(R.id.mediumButton)
        hardButton = findViewById(R.id.hardButton)
    }

    private fun setupGame() {
        gameManager = GameManager()

        easyButton.setOnClickListener { gameManager.startGame(Difficulty.EASY) }
        mediumButton.setOnClickListener { gameManager.startGame(Difficulty.MEDIUM) }
        hardButton.setOnClickListener { gameManager.startGame(Difficulty.HARD) }

        confirmButton.setOnClickListener { gameManager.confirmInput() }
        clearButton.setOnClickListener { gameManager.clearInput() }

        perfectButton.setOnClickListener { checkAnswer(true) }
        notPerfectButton.setOnClickListener { checkAnswer(false) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeAR()
            } else {
                Toast.makeText(this, "Kamera izni gerekli", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun observeGameState() {
        gameManager.gameState.observe(this) { state ->
            currentNumberText.text = getString(R.string.text_current_number, state.currentNumber)
            factorsText.text = getString(R.string.text_selected_factors,
                state.selectedFactors.joinToString(", "))
            sumText.text = getString(R.string.text_sum, state.factorsSum)
            tempInputText.text = getString(R.string.text_input, state.currentInput)

            if (state.timeRemaining > 0) {
                timerText.text = getString(R.string.text_timer, state.timeRemaining)
            }

            if (state.timeRemaining == 0 && state.difficulty != Difficulty.EASY) {
                showTimeUpDialog()
            }
        }
    }

    private fun checkAnswer(isPerfectGuess: Boolean) {
        if (gameManager.checkAnswer(isPerfectGuess)) {
            arSceneManager.showSuccessAnimation()
            Toast.makeText(this, R.string.toast_correct, Toast.LENGTH_SHORT).show()
            gameManager.startGame(gameManager.gameState.value?.difficulty ?: Difficulty.EASY)
        } else {
            Toast.makeText(this, R.string.toast_wrong, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTimeUpDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_time_up_title)
            .setMessage(R.string.dialog_time_up_message)
            .setPositiveButton(R.string.dialog_btn_yes) { _, _ ->
                gameManager.startGame(gameManager.gameState.value?.difficulty ?: Difficulty.EASY)
            }
            .setNegativeButton(R.string.dialog_btn_no) { _, _ ->
                gameManager.endGame()
            }
            .setCancelable(false)
            .show()
    }

    override fun onResume() {
        super.onResume()
        if (::arFragment.isInitialized) {
            arFragment.arSceneView?.resume()
        } else {
            checkArCore()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::arFragment.isInitialized) {
            arFragment.arSceneView?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::gameManager.isInitialized) {
            gameManager.endGame()
        }
        if (::arFragment.isInitialized) {
            arFragment.arSceneView?.destroy()
        }
    }
}