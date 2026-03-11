package dev.faridguliyev.replaydroid.dsl

import android.util.Log
import dev.faridguliyev.replaydroid.Config
import dev.faridguliyev.replaydroid.Diagnostics

fun Diagnostics.Companion.build(
    scope: DiagnosticsBuilderScope.() -> Unit
): Diagnostics {
    val config = Config(
        enabledFeatures = setOf(),
        captureFramesMaxBytes = 1024 * 1024, // 1 MB
        logEventsMaxSize = 1000
    )

    scope(DiagnosticsBuilderScopeImpl(config))
    Log.i("ReplayDroid", "Diagnostics created with config: $config")
    return Diagnostics(config)
}