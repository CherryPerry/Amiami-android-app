package ru.cherryperry.amiami.domain.performance

import java.io.Closeable

/**
 * This class is used to make performance measuring any way You want.
 *
 * There is two ways to use it in kotlin:
 * ```kotlin
 * startTrace().use { measureBlock() }
 * ```
 * and
 * ```kotlin
 * startTrace()
 * measureBlock()
 * stopTrace()
 * ```
 */
interface PerformanceTracer : Closeable {

    fun startTrace(name: String, attributes: Map<String, String> = emptyMap()): PerformanceTracer

    fun stopTrace()

    override fun close() = stopTrace()
}
