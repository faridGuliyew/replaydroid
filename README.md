ReplayDroid 📹🛡️

ReplayDroid is a lightweight, non-intrusive Android library designed for deep application diagnostics. It helps developers and QA teams understand exactly what happened leading up to a bug by capturing visual frames, monitoring Activity lifecycles, and logging system events in a "flight recorder" fashion.

Key Features:

🖼️ Frame Capture: Captures snapshots of the app's UI to reconstruct user actions - IDLE frames are skipped.

🔄 Lifecycle Tracking: Automatically logs Activity lifecycle transitions.

🚀 Performance-First: Designed to run in the background with minimal overhead on the main thread.

Upcoming Features:

📝 Event Logging: Capturing intent data, and system broadcasts.

# Installation

ReplayDroid is hosted on JitPack. Follow these steps to include it in your project:

1. Add the JitPack repository

Add it to your root build.gradle.kts (or settings.gradle.kts for newer projects):
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```
2. Add the dependency

Add the following to your app-level build.gradle.kts:
```kotlin
dependencies {
    // Latest version
    implementation("com.github.faridGuliyew:replaydroid:1.0.0-DEV")
}
```


# Quick Start
Check **sample-app** module for detailed example usage.


1. Create an instance of Diagnostics class and enable/configure your desired features there using DSL.
```kotlin
val diagnostics by lazy {
    Diagnostics.build {
        install(feature = DiagnosticsFeature.CaptureFrames)
        install(feature = DiagnosticsFeature.LogEvents)
        install(feature = DiagnosticsFeature.Debug)
    }
}
```

2. Implement DiagnosticsTransport interface to send diagnostic result to your backend.
```kotlin
val transport = object : DiagnosticsTransport {
    override fun sendDiagnostics(result: DiagnosticsResult) {
        // Serialize and send $result here. Sample app includes an example using ktor client and multipart.
    }
}
```

3. Initialize the Library
The best place to initialize ReplayDroid is in your Application class:

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        diagnostics.initialize(this, transport)
    }
}
```

# How it works
ReplayDroid uses ActivityLifecycleCallbacks to hook into your app's lifecycle without requiring any changes to your Activity code. It utilizes a background thread to process frame captures, ensuring your UI remains buttery smooth.



# License

Copyright 2024 Farid Guliyev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
