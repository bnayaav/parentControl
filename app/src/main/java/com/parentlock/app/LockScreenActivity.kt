package com.parentlock.app

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.Timer
import java.util.TimerTask

class LockScreenActivity : AppCompatActivity() {
    private var updateTimer: Timer? = null
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_lock_screen)
        val prefs = getSharedPreferences("parentlock", MODE_PRIVATE)
        val secretKey = prefs.getString("secret_key", "") ?: ""
        val displayCode = findViewById<TextView>(R.id.displayCode)
        val timerText = findViewById<TextView>(R.id.timerText)
        val unlockInput = findViewById<EditText>(R.id.unlockInput)
        val unlockBtn = findViewById<Button>(R.id.unlockBtn)
        val errorText = findViewById<TextView>(R.id.errorText)

        fun refresh() { scope.launch { displayCode.text = withContext(Dispatchers.Default) { TOTPHelper.generateChildCode(secretKey) } } }
        refresh()

        var lastWindow = TOTPHelper.getTimeWindow()
        updateTimer = Timer()
        updateTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() = runOnUiThread {
                timerText.text = "קוד חדש בעוד "+ ${TOTPHelper.getSecondsRemaining()} + " שניות"
                val w = TOTPHelper.getTimeWindow()
                if (w != lastWindow) { lastWindow = w; refresh() }
            }
        }, 0, 1000)

        unlockBtn.setOnClickListener {
            scope.launch {
                val valid = withContext(Dispatchers.Default) { TOTPHelper.verifyUnlockCode(secretKey, unlockInput.text.toString().trim()) }
                if (valid) { LockState.isLocked = false; finish() }
                else { errorText.text = "קוד שגוי"; errorText.visibility = View.VISIBLE; unlockInput.text.clear() }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {}
    override fun onDestroy() { super.onDestroy(); updateTimer?.cancel(); scope.cancel() }
}