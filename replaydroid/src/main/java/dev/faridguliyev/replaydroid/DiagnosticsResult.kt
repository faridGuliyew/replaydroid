package dev.faridguliyev.replaydroid


class DiagnosticsResult (
    val frames: List<ByteArray>,
    val frameTimestamps: List<Long>,
    val events: List<DiagnosticEvent>,
    val deviceInfo: DeviceInfo?
)