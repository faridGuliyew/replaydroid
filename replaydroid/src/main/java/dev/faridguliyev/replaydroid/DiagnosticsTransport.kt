package dev.faridguliyev.replaydroid

interface DiagnosticsTransport {
    fun sendDiagnostics(result: DiagnosticsResult)
}