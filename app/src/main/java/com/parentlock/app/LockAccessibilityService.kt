package com.parentlock.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class LockAccessibilityService : AccessibilityService() {
    private val TARGET = setOf("com.whatsapp", "com.whatsapp.w4b")
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg in TARGET && LockState.isLocked) {
            startActivity(Intent(applicationContext, LockScreenActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }
    override fun onInterrupt() {}
}