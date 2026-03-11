package dev.faridguliyev.replaydroid.features

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Size
import androidx.core.graphics.createBitmap
import dev.faridguliyev.replaydroid.utils.getActivityInfo
import dev.faridguliyev.replaydroid.utils.throttleFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume

/**
 * @param captureRequestsChannel - This channel is observed internally, which captures views of activities when .send(activity) invoked.
 * @param reusableBitmap - Bitmap used to store captured views. Allocated once & reused for every capture.
 * @param reusableBitmapSize - Indicates size for Bitmap for reuse. If size changes, new bitmap is allocated
 */
class CaptureFramesFeatureModule(
    val coroutineScope: CoroutineScope,
    val debugger: DebugFeatureModule? = null,
    val onFrameReady: (frame: ByteArray, timestamp: Long) -> Unit
) {
    private val captureRequestsChannel = Channel<Activity>()
    private var reusableBitmap: Bitmap = createBitmap(1, 1)
    private var reusableBitmapSize: Size = Size(1, 1)

    init {
        observeAndProcessCaptureRequests()
    }

    fun makeCaptureRequest(activity: Activity) {
        captureRequestsChannel.trySend(activity)
    }

    private fun observeAndProcessCaptureRequests() {
        coroutineScope.launch {
            captureRequestsChannel
                .receiveAsFlow()
                .throttleFirst(100L)
                .collect {
                    delay(10)
                    val timestamp = System.currentTimeMillis()
                    val capturedView = captureView(it) ?: return@collect
                    saveBitmapToRegistry(
                        bitmap = capturedView,
                        timestamp = timestamp
                    )
                }
        }
    }

    private fun saveBitmapToRegistry(
        bitmap: Bitmap,
        timestamp: Long
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream)
            val bytes = stream.toByteArray()
            onFrameReady(bytes, timestamp)

            debugger?.logInfo("CaptureFramesFeatureModule -> saveBitmapToRegistry() -> Captured bitmap JPEG size in bytes: ${bytes.size}")
        }
    }

    private suspend fun captureView(activity: Activity): Bitmap? {
        return suspendCancellableCoroutine { cont ->
            try {
                val rootView = activity.window.decorView.rootView!!

                val scale = 0.25F
                val width = (rootView.width * scale).toInt()
                val height = (rootView.height * scale).toInt()

                val bitmap =
                    if (reusableBitmapSize.width == width && reusableBitmapSize.height == height) {
                        reusableBitmap
                    } else {
                        createBitmap(width, height, Bitmap.Config.RGB_565).also {
                            reusableBitmap = it
                            reusableBitmapSize = Size(width, height)
                        }
                    }

                rootView.post {
                    rootView.draw(Canvas(bitmap).apply { scale(scale, scale) })
                    cont.resume(bitmap)
                }
            } catch (e: Exception) {
                debugger?.logError("CaptureFramesFeatureModule -> captureView() -> Failed to capture ${activity.getActivityInfo()}. Reason: ${e.message}")
                cont.resume(null)
            }
        }
    }
}