package dev.faridguliyev.replaydroid

interface DiagnosticsTransport {
    suspend fun sendDiagnostics(result: DiagnosticsResult) : Boolean
}