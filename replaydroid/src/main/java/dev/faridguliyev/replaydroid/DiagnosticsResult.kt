package dev.faridguliyev.replaydroid


data class DiagnosticsResult (
    val frames: List<ByteArray>,
    val frameTimestamps: List<Long>,
    val events: List<DiagnosticEvent>,
    val deviceInfo: DeviceInfo?,
    val extras: Map<String, String>
)