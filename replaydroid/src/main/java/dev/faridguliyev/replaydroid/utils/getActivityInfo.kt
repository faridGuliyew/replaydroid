package dev.faridguliyev.replaydroid.utils

import android.app.Activity
import dev.faridguliyev.replaydroid.ActivityInfo

fun Activity.getActivityInfo() : ActivityInfo {
    return ActivityInfo(
        className = this::class.simpleName.orEmpty()
    )
}