package dev.faridguliyev.replaydroid

import dev.faridguliyev.replaydroid.features.Feature

data class Config(
    var enabledFeatures: Set<Feature>,
    var captureFramesMaxBytes: Int,
    var logEventsMaxSize: Int
)