package dev.faridguliyev.replaydroid.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.throttleFirst(windowMillis: Long): Flow<T> = flow {
    var lastTime = 0L
    collect { value ->
        val now = System.currentTimeMillis()
        if (now - lastTime >= windowMillis) {
            lastTime = now
            emit(value)
        } else {
//            Log.e("ReplayDroid", "Capture request dropped (reason: too fast)")
        }
    }
}