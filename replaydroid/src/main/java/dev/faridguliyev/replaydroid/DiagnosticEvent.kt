package dev.faridguliyev.replaydroid

import android.app.Activity

sealed interface DiagnosticEventType {
    fun getParams() : Map<String, String>
    data class ActivityLifecycle(
        val activity: Activity,
        val lifecycleEvent: String
    ) : DiagnosticEventType {
        override fun getParams(): Map<String, String> {
            return mapOf(
                "activity" to activity::class.simpleName.orEmpty(),
                "lifecycleEvent" to lifecycleEvent
            )
        }
    }

    data class AppCrash(val throwable: Throwable) : DiagnosticEventType {
        override fun getParams(): Map<String, String> {
            return mapOf(
                "stacktrace" to throwable.stackTraceToString()
            )
        }
    }
}

data class DiagnosticEvent (
    val type: String,
    val timestamp: Long,
    val params: Map<String, String> = mapOf()
)