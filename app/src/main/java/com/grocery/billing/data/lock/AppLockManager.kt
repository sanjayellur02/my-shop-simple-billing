package com.grocery.billing.data.lock

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Stores the app lock PIN (as a salted SHA-256 hash) and the fingerprint
 * preference in SharedPreferences.
 */
class AppLockManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_lock", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PIN_HASH = "pin_hash"
        private const val KEY_PIN_SALT = "pin_salt"
        private const val KEY_FINGERPRINT = "fingerprint_enabled"
    }

    fun isPinSet(): Boolean = !prefs.getString(KEY_PIN_HASH, "").orEmpty().isEmpty()

    fun isFingerprintEnabled(): Boolean = prefs.getBoolean(KEY_FINGERPRINT, false)

    fun setPin(pin: String) {
        val salt = randomSalt()
        prefs.edit()
            .putString(KEY_PIN_SALT, salt)
            .putString(KEY_PIN_HASH, hash(pin, salt))
            .apply()
    }

    fun verifyPin(pin: String): Boolean {
        val salt = prefs.getString(KEY_PIN_SALT, null) ?: return false
        val expected = prefs.getString(KEY_PIN_HASH, null) ?: return false
        return hash(pin, salt) == expected
    }

    fun setFingerprintEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_FINGERPRINT, enabled).apply()
    }

    fun disable() {
        prefs.edit().clear().apply()
    }

    private fun hash(pin: String, salt: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest((pin + salt).toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

    private fun randomSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
