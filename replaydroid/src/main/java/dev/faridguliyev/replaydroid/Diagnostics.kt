package dev.faridguliyev.replaydroid

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import androidx.lifecycle.Lifecycle
import dev.faridguliyev.replaydroid.features.CaptureFramesFeatureModule
import dev.faridguliyev.replaydroid.features.DebugFeatureModule
import dev.faridguliyev.replaydroid.features.Feature
import dev.faridguliyev.replaydroid.features.LogEventsFeatureModule
import dev.faridguliyev.replaydroid.registries.DiagnosticsEventRegistry
import dev.faridguliyev.replaydroid.registries.DiagnosticsFrameRegistry
import dev.faridguliyev.replaydroid.utils.fetchDeviceInfo
import dev.faridguliyev.replaydroid.utils.getActivityInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** @param application - Used for registering activity lifecycle callbacks & thus get currently active activity
 * @param frameRegistry - Stores captured frames
 * @param eventRegistry - Stores captured events
 * @param coroutineScope - CoroutineScope of this class, which lives through the application
 * @param windowCallbackRegisteredActivities - Set of activities for which window callback is set. It is used to prevent setting multiple callbacks for the same activity.
 *
 * */

class Diagnostics(val config: Config) {
    companion object {}

    private lateinit var application: Application
    private var transport: DiagnosticsTransport? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val debugger = DebugFeatureModule()
        .takeIf { Feature.DEBUG in config.enabledFeatures }

    // Registries
    private val eventRegistry = DiagnosticsEventRegistry(
        debugger = debugger,
        maxEventSize = config.logEventsMaxSize
    )
    val frameRegistry = DiagnosticsFrameRegistry(
        debugger = debugger,
        maxBytes = config.captureFramesMaxBytes
    )

    val captureFramesFeatureModule = CaptureFramesFeatureModule(
        coroutineScope = coroutineScope,
        debugger = debugger,
        onFrameReady = frameRegistry::addFrame
    ).takeIf { Feature.CAPTURE_FRAMES in config.enabledFeatures }

    val logEventsFeatureModule = LogEventsFeatureModule(
        debugger = debugger,
        onEventReady = eventRegistry::addEvent
    ).takeIf { Feature.LOG_EVENTS in config.enabledFeatures }

    private val windowCallbackRegisteredActivities: MutableSet<ActivityInfo> = mutableSetOf()

    fun initialize(application: Application, transport: DiagnosticsTransport?) {
        this.application = application
        this.transport = transport

        registerActivityCallbacks()
    }

    private fun registerUiCapturesOnTouchEvents(activity: Activity) {
        val activityInfo = activity.getActivityInfo()

        if (activityInfo in windowCallbackRegisteredActivities) {
            debugger?.logInfo("registerUiCapturesOnTouchEvents() -> Listener already registered. Skipping $activityInfo")
            return
        }

        debugger?.logInfo("registerUiCapturesOnTouchEvents() -> Listener registered for $activityInfo")

        val originalCallback = activity.window.callback
        windowCallbackRegisteredActivities.add(activityInfo)
        activity.window.callback = object : Window.Callback by originalCallback {
            override fun dispatchTouchEvent(p0: MotionEvent?): Boolean {
                debugger?.logInfo("dispatchTouchEvent()")
                captureFramesFeatureModule?.makeCaptureRequest(activity)

                return originalCallback.dispatchTouchEvent(p0)
            }
        }
    }

    private fun onActivityResumed(activity: Activity) {
        captureFramesFeatureModule?.makeCaptureRequest(activity) // Capture immediately when activity changes
        registerUiCapturesOnTouchEvents(activity) // Register listener to capture when user interacts.
    }

    private fun onActivityDestroyed(activity: Activity) {
        windowCallbackRegisteredActivities.remove(activity.getActivityInfo())
    }

    private fun Activity.logLifecycleEvent(
        lifecycleEvent: Lifecycle.Event
    ) {
        logEventsFeatureModule?.addLifecycleEvent(this, lifecycleEvent)
    }

    private fun registerActivityCallbacks() {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_CREATE)
                }

                override fun onActivityDestroyed(p0: Activity) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    this@Diagnostics.onActivityDestroyed(p0)
                }

                override fun onActivityPaused(p0: Activity) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                }

                override fun onActivityResumed(p0: Activity) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    this@Diagnostics.onActivityResumed(p0)
                }

                override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

                override fun onActivityStarted(p0: Activity) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_START)
                }

                override fun onActivityStopped(p0: Activity) {
                    p0.logLifecycleEvent(Lifecycle.Event.ON_STOP)
                }
            }
        )
    }

    fun getResult(): DiagnosticsResult {
        val (frames, timestamps) = frameRegistry.getFramesAndTimestamps()

        return DiagnosticsResult(
            deviceInfo = application.fetchDeviceInfo(),
            frames = frames,
            frameTimestamps = timestamps,
            events = eventRegistry.getEvents()
        )
    }

    fun sendDiagnostics() {
        transport?.sendDiagnostics(getResult())

        if (transport == null) {
            debugger?.logError("sendDiagnostics() failed. Transport is not configured.")
        } else {
            debugger?.log("Diagnostics sent over $transport")
        }
    }
}