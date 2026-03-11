package dev.faridguliyev.replaydroid.registries

import android.util.Log
import dev.faridguliyev.replaydroid.DiagnosticEvent
import dev.faridguliyev.replaydroid.features.DebugFeatureModule

class DiagnosticsEventRegistry (
    val debugger: DebugFeatureModule?,
    val maxEventSize: Int
) {
    private val events: MutableList<DiagnosticEvent> = mutableListOf()

    // TODO - Optimize this operation
    fun addEvent(event: DiagnosticEvent) {
        if (events.size >= maxEventSize) {
            debugger?.logError("DiagnosticsEventRegistry reached its max capacity. Event count: ${events.size}. Coverage(ms): ${events.last().timestamp - events.first().timestamp} ms")
            events.removeAt(0)
        }

        events.add(event)
    }

    fun getEvents() : List<DiagnosticEvent> = events

    fun clear() {
        events.clear()
    }
}