package dev.faridguliyev.replaydroid

data class DeviceInfo(
    // App Specs
    val appVersionName: String,
    val appVersionCode: Long,
    val packageName: String,

    // Hardware details
    val manufacturer: String,
    val model: String,
    val device: String,
    val board: String,
    val hardware: String,

    // Software details
    val osVersion: String,
    val sdkInt: Int,
    val buildFingerprint: String, // Unique string for the specific build

    // Display / UI
    val screenWidth: Int,
    val screenHeight: Int,
    val densityDpi: Int,

    // Runtime info
    val totalMemoryMb: Long,
    val availableMemoryMb: Long,
    val isLowRamDevice: Boolean,

    val batteryLevel: Int,
    val isCharging: Boolean
)