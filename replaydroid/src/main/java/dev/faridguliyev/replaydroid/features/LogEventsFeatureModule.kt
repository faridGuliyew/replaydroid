package dev.faridguliyev.replaydroid.features

import android.app.Activity
import androidx.lifecycle.Lifecycle
import dev.faridguliyev.replaydroid.DiagnosticEvent
import dev.faridguliyev.replaydroid.DiagnosticEventType

class LogEventsFeatureModule (
    val debugger: DebugFeatureModule? = null,
    val onEventReady: (event: DiagnosticEvent) -> Unit,
    val onAppCrash: () -> Unit
) {
    init {
        registerGlobalExceptionHandler()
    }
    fun registerGlobalExceptionHandler() {
        val currentExceptionListener : Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            addEvent(type = DiagnosticEventType.AppCrash(throwable = throwable))
            onAppCrash()
            currentExceptionListener?.uncaughtException(thread, throwable)
        }
    }
    fun addEvent(type: DiagnosticEventType) {
        val event = DiagnosticEvent(
            type = type::class.simpleName.orEmpty(),
            timestamp = System.currentTimeMillis(),
            params = type.getParams()
        )
        onEventReady(event)
        debugger?.logInfo("LogEventsFeatureModule -> addEvent() -> EVENT LOGGED: $type")
    }
    fun addLifecycleEvent(
        activity: Activity,
        lifecycleEvent: Lifecycle.Event
    ) {
        addEvent(
            type = DiagnosticEventType.ActivityLifecycle(
                activity = activity,
                lifecycleEvent = lifecycleEvent.name
            )
        )
    }
}