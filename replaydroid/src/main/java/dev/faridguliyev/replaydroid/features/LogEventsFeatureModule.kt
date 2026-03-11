package dev.faridguliyev.replaydroid.features

import android.app.Activity
import androidx.lifecycle.Lifecycle
import dev.faridguliyev.replaydroid.DiagnosticEvent
import dev.faridguliyev.replaydroid.DiagnosticEventType
import dev.faridguliyev.replaydroid.utils.getActivityInfo

class LogEventsFeatureModule (
    val debugger: DebugFeatureModule? = null,
    val onEventReady: (event: DiagnosticEvent) -> Unit
) {
    fun addEvent(
        type: DiagnosticEventType,
        activity: Activity
    ) {
        val event = DiagnosticEvent(
            type = type::class.simpleName.orEmpty(),
            activityInfo = activity.getActivityInfo(),
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
            type = DiagnosticEventType.ActivityLifecycle(lifecycleEvent = lifecycleEvent.name),
            activity = activity
        )
    }
}