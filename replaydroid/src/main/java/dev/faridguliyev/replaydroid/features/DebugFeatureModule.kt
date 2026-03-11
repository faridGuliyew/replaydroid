package dev.faridguliyev.replaydroid.features

import android.util.Log

class DebugFeatureModule {
    companion object { private const val TAG = "ReplayDroid" }
    fun log(message: String) {
        Log.d(TAG, message)
    }
    fun logError(message: String) {
        Log.e(TAG, message)
    }
    fun logInfo(message: String) {
        Log.i(TAG, message)
    }
}