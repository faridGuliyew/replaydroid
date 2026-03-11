package dev.faridguliyev.sample_app

import android.app.Application
import dev.faridguliyev.replaydroid.Diagnostics
import dev.faridguliyev.replaydroid.dsl.DiagnosticsFeature
import dev.faridguliyev.replaydroid.dsl.build
import io.ktor.utils.io.InternalAPI

val diagnostics by lazy {
    Diagnostics.build {
        install(feature = DiagnosticsFeature.CaptureFrames)
        install(feature = DiagnosticsFeature.LogEvents)
        install(feature = DiagnosticsFeature.Debug)
    }
}

class App : Application() {
    @OptIn(InternalAPI::class)
    override fun onCreate() {
        super.onCreate()

        diagnostics.initialize(this, transport)
    }
}