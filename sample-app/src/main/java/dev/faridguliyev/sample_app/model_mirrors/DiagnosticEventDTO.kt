package dev.faridguliyev.sample_app.model_mirrors

import dev.faridguliyev.replaydroid.DeviceInfo
import dev.faridguliyev.replaydroid.DiagnosticEvent
import dev.faridguliyev.replaydroid.DiagnosticsResult
import kotlinx.serialization.Serializable

// DTO MIRRORS

// 1. Create a single DTO for the main event class
@Serializable
data class DiagnosticEventDTO(
    val type: String,
    val timestamp: Long,
    val params: Map<String, String>
)

@Serializable
data class ActivityInfoDTO(
    val className: String
)

// 2. Simple mapping extension
fun DiagnosticEvent.toDTO() = DiagnosticEventDTO(
    type = this.type,
    timestamp = this.timestamp,
    params = this.params
)

@Serializable
data class DiagnosticsPartialResultDTO(
    val deviceInfo: DeviceInfoDto?,
    val frameTimestamps: List<Long>,
    val events: List<DiagnosticEventDTO>
)

@Serializable
data class DiagnosticsResultDTO(
    val deviceInfo: DeviceInfoDto?,
    val frames: List<ByteArray>,
    val frameTimestamps: List<Long>,
    val events: List<DiagnosticEventDTO>
)

fun DiagnosticsResult.toDTO() = DiagnosticsResultDTO(
    deviceInfo = deviceInfo?.toDTO(),
    frames = this.frames,
    frameTimestamps = this.frameTimestamps,
    events = this.events.map { it.toDTO() }
)

@Serializable
data class DeviceInfoDto(
    // App Specs
    val appVersionName: String,
    val appVersionCode: Long,
    val packageName: String,

    // Hardware basics
    val manufacturer: String,
    val model: String,
    val device: String,
    val board: String,
    val hardware: String,

    // Software details
    val osVersion: String, // e.g., "14"
    val sdkInt: Int,       // e.g., 34
    val buildFingerprint: String, // Unique string for the specific build

    // Display / UI
    val screenWidth: Int,
    val screenHeight: Int,
    val densityDpi: Int,

    // Runtime info (Crucial for diagnostics)
    val totalMemoryMb: Long,
    val availableMemoryMb: Long,
    val isLowRamDevice: Boolean,

    val batteryLevel: Int, // Percentage 0-100
    val isCharging: Boolean
)

fun DeviceInfo.toDTO() = DeviceInfoDto(
    appVersionName = appVersionName,
    appVersionCode = appVersionCode,
    packageName = packageName,
    manufacturer = manufacturer,
    model = model,
    device = device,
    board = board,
    hardware = hardware,
    osVersion = osVersion,
    sdkInt = sdkInt,
    buildFingerprint = buildFingerprint,
    screenWidth = screenWidth,
    screenHeight = screenHeight,
    densityDpi = densityDpi,
    totalMemoryMb = totalMemoryMb,
    availableMemoryMb = availableMemoryMb,
    isLowRamDevice = isLowRamDevice,
    batteryLevel = batteryLevel,
    isCharging = isCharging
)