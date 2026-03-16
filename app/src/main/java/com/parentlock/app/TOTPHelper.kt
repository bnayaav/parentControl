package com.parentlock.app

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs

object TOTPHelper {
    const val PERIOD_SEC = 120L

    fun getTimeWindow(): Long = System.currentTimeMillis() / 1000 / PERIOD_SEC

    fun getSecondsRemaining(): Long {
        val elapsed = (System.currentTimeMillis() / 1000) % PERIOD_SEC
        return PERIOD_SEC - elapsed
    }

    fun generateChildCode(secretKey: String, window: Long = getTimeWindow()): String {
        return (hotp(secretKey, "CHILD:$window") % 10000).toString().padStart(4, '0')
    }

    fun generateUnlockCode(secretKey: String, window: Long = getTimeWindow()): String {
        return (hotp(secretKey, "UNLOCK:$window") % 1000000).toString().padStart(6, '0')
    }

    fun verifyUnlockCode(secretKey: String, input: String): Boolean {
        val now = getTimeWindow()
        return listOf(now, now - 1).any { generateUnlockCode(secretKey, it) == input }
    }

    private fun hotp(keyHex: String, message: String): Long {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(hexToBytes(keyHex), "HmacSHA256"))
        val hash = mac.doFinal(message.toByteArray(Charsets.UTF_8))
        val offset = hash.last().toInt() and 0x0f
        return abs(((hash[offset].toInt() and 0x7f) shl 24) or
                   ((hash[offset+1].toInt() and 0xff) shl 16) or
                   ((hash[offset+2].toInt() and 0xff) shl 8) or
                   (hash[offset+3].toInt() and 0xff).toLong())
    }

    private fun hexToBytes(hex: String) = ByteArray(hex.length / 2) {
        hex.substring(it * 2, it * 2 + 2).toInt(16).toByte()
    }
}