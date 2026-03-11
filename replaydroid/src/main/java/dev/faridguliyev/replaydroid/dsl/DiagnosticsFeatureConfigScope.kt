package dev.faridguliyev.replaydroid.dsl

sealed interface DiagnosticsFeatureConfigScope {
    interface CaptureFramesConfigScope : DiagnosticsFeatureConfigScope {
        fun setFrameCaptureLimit(maxBytes: Int)
    }

    interface LogEventsConfigScope : DiagnosticsFeatureConfigScope {
        fun setEventSizeLimit(maxSize: Int)
    }

    interface NoOpScope : DiagnosticsFeatureConfigScope
}