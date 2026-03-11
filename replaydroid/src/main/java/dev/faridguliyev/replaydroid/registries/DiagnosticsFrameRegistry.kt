package dev.faridguliyev.replaydroid.registries

import dev.faridguliyev.replaydroid.features.DebugFeatureModule

class DiagnosticsFrameRegistry (
    val debugger: DebugFeatureModule?,
    val maxBytes: Int
) {
    private val frames: MutableList<ByteArray> = mutableListOf()
    private val frameStamps: MutableList<Long> = mutableListOf()
    private var totalSize: Int = 0

    // TODO - Optimize this operation
    fun addFrame(frame: ByteArray, timestamp: Long) {
        if (totalSize + frame.size > maxBytes) {
            debugger?.logError("DiagnosticsFrameRegistry reached its max capacity. Frame count: ${frames.size}, Frame coverage (ms): ${frameStamps.last() - frameStamps.first()} ms")
            // Remove first frame
            totalSize -= frames[0].size
            frames.removeAt(0)
            frameStamps.removeAt(0)
        }

        totalSize += frame.size
        frames.add(frame)
        frameStamps.add(timestamp)

        debugger?.logInfo("DiagnosticsFrameRegistry current capacity: $totalSize")
    }

    fun clear() {
        totalSize = 0
        frames.clear()
        frameStamps.clear()
    }

    fun getFramesAndTimestamps() : Pair<List<ByteArray>, List<Long>> {
        return frames to frameStamps
    }
}