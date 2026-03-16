package com.parentlock.app

import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("parentlock", MODE_PRIVATE)
        val si = Intent(this, LockService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(si) else startService(si)
        val statusText = findViewById<TextView>(R.id.statusText)
        statusText.text = if (LockState.isLocked) "🔒 WhatsApp נעול" else "🔓 WhatsApp פתוח"
        findViewById<Button>(R.id.lockNowBtn).setOnClickListener {
            LockState.isLocked = true
            (getSystemService(ACTIVITY_SERVICE) as ActivityManager).killBackgroundProcesses("com.whatsapp")
            statusText.text = "🔒 WhatsApp נעול"
        }
        findViewById<TextView>(R.id.secretKeyText).text = prefs.getString("secret_key", "")
        if (!isAccessibilityEnabled()) AlertDialog.Builder(this)
            .setTitle("נדרשת הרשאה").setMessage("הפעל שירות נגישות של ParentLock")
            .setPositiveButton("פתח") { _, _ -> startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
            .setNegativeButton("אחר כך", null).show()
    }

    private fun isAccessibilityEnabled(): Boolean {
        val service = "${packageName}/${LockAccessibilityService::class.java.canonicalName}"
        return (Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: "").contains(service)
    }
}
