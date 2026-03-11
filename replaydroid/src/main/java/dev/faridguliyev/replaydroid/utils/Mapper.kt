package dev.faridguliyev.replaydroid.utils

import dev.faridguliyev.replaydroid.dsl.DiagnosticsFeature
import dev.faridguliyev.replaydroid.features.Feature

val DiagnosticsFeature<*>.type
    get() = when (this) {
        DiagnosticsFeature.CaptureFrames -> Feature.CAPTURE_FRAMES
        DiagnosticsFeature.LogEvents -> Feature.LOG_EVENTS
        DiagnosticsFeature.Debug -> Feature.DEBUG
    }