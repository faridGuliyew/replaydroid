package dev.faridguliyev.replaydroid

sealed interface DiagnosticEventType {
    fun getParams() : Map<String, String>
    data class ActivityLifecycle(val lifecycleEvent: String) : DiagnosticEventType {
        override fun getParams(): Map<String, String> {
            return mapOf("lifecycleEvent" to lifecycleEvent)
        }
    }
}

data class DiagnosticEvent (
    val type: String,
    val timestamp: Long,
    val activityInfo: ActivityInfo,
    val params: Map<String, String> = mapOf()
)