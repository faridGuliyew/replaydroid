package dev.faridguliyev.replaydroid.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import dev.faridguliyev.replaydroid.DeviceInfo

fun Context.fetchDeviceInfo(): DeviceInfo? {
    return runCatching {
        // Memory info
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        // Display info
        val displayMetrics = resources.displayMetrics

        // Package info
        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong()

        // Battery info
        val batteryStatus: Intent? =
            registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = (level / scale.toFloat() * 100).toInt()
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        return DeviceInfo(
            packageName = packageName,
            appVersionName = packageInfo.versionName ?: "unknown",
            appVersionCode = versionCode,

            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            device = Build.DEVICE,
            board = Build.BOARD,
            hardware = Build.HARDWARE,

            osVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            buildFingerprint = Build.FINGERPRINT,

            screenWidth = displayMetrics.widthPixels,
            screenHeight = displayMetrics.heightPixels,
            densityDpi = displayMetrics.densityDpi,

            totalMemoryMb = memoryInfo.totalMem / (1024 * 1024),
            availableMemoryMb = memoryInfo.availMem / (1024 * 1024),
            isLowRamDevice = activityManager.isLowRamDevice,

            batteryLevel = batteryPct,
            isCharging = isCharging
        )
    }.getOrNull()
}