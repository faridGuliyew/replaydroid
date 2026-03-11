package dev.faridguliyev.replaydroid.dsl

sealed interface DiagnosticsFeature <S: DiagnosticsFeatureConfigScope> {
    data object CaptureFrames : DiagnosticsFeature<DiagnosticsFeatureConfigScope.CaptureFramesConfigScope>
    data object LogEvents : DiagnosticsFeature<DiagnosticsFeatureConfigScope.LogEventsConfigScope>
    data object Debug : DiagnosticsFeature<DiagnosticsFeatureConfigScope.NoOpScope>
}