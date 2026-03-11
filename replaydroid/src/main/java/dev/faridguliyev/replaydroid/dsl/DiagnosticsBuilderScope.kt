package dev.faridguliyev.replaydroid.dsl

import dev.faridguliyev.replaydroid.Config
import dev.faridguliyev.replaydroid.utils.type
import kotlin.collections.plus

interface DiagnosticsBuilderScope {
    fun <S : DiagnosticsFeatureConfigScope> install(feature: DiagnosticsFeature<S>, block: S.() -> Unit = {})
}

class DiagnosticsBuilderScopeImpl (private val config: Config) : DiagnosticsBuilderScope {
    override fun <S : DiagnosticsFeatureConfigScope> install(
        feature: DiagnosticsFeature<S>,
        block: S.() -> Unit
    ) {
        config.enabledFeatures += feature.type

        when (feature) {
            DiagnosticsFeature.CaptureFrames -> {
                val featureScopeImpl = object : DiagnosticsFeatureConfigScope.CaptureFramesConfigScope {
                    override fun setFrameCaptureLimit(maxBytes: Int) {
                        config.captureFramesMaxBytes = maxBytes
                    }
                }
                block.invoke(featureScopeImpl as S)
            }

            DiagnosticsFeature.LogEvents -> {
                val featureScopeImpl = object : DiagnosticsFeatureConfigScope.LogEventsConfigScope {
                    override fun setEventSizeLimit(maxSize: Int) {
                        config.logEventsMaxSize = maxSize
                    }
                }
                block.invoke(featureScopeImpl as S)
            }

            DiagnosticsFeature.Debug -> {

            }
        }
    }
}