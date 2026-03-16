package com.parentlock.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.security.SecureRandom

class SetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("parentlock", MODE_PRIVATE)
        if (prefs.contains("secret_key")) { startActivity(Intent(this, MainActivity::class.java)); finish(); return }
        setContentView(R.layout.activity_setup)
        val bytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val secret = bytes.joinToString("") { "%02X".format(it) }
        findViewById<TextView>(R.id.secretDisplay).text = secret
        val secretInput = findViewById<EditText>(R.id.secretInput)
        val errorText = findViewById<TextView>(R.id.errorText)
        findViewById<Button>(R.id.confirmBtn).setOnClickListener {
            if (secretInput.text.toString().trim().uppercase() == secret) {
                prefs.edit().putString("secret_key", secret).apply()
                startActivity(Intent(this, MainActivity::class.java)); finish()
            } else { errorText.text = "המפתח לא תואם"; errorText.visibility = View.VISIBLE }
        }
    }
}