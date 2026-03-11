package dev.faridguliyev.sample_app

import dev.faridguliyev.sample_app.model_mirrors.DiagnosticsPartialResultDTO
import dev.faridguliyev.sample_app.model_mirrors.toDTO
import dev.faridguliyev.replaydroid.DiagnosticsResult
import dev.faridguliyev.replaydroid.DiagnosticsTransport
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

val transport = object : DiagnosticsTransport {
    override suspend fun sendDiagnostics(result: DiagnosticsResult): Boolean {
//        CoroutineScope(Dispatchers.IO).launch {
            val dto = result.toDTO()
            val partialDto = DiagnosticsPartialResultDTO(
                deviceInfo = dto.deviceInfo,
                frameTimestamps = dto.frameTimestamps,
                events = dto.events
            )
            client.submitFormWithBinaryData(
                url = "http://10.0.2.2:8080/upload-diagnostics",
                formData = formData {
                    val jsonMetadata = Json.encodeToString(partialDto)
                    append("metadata", jsonMetadata)

                    // 2. Frames using the specific ByteArray append overload
                    result.frames.toList().forEachIndexed { index, bytes ->
                        append("frame_$index", bytes, Headers.build {
                            // Ktor handles the Content-Type automatically if you
                            // provide a filename or use this specific structure
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"frame_$index\"; filename=\"frame_$index.bin\"")
                        })
                    }
                }
            )
        return true
//        }
    }
}