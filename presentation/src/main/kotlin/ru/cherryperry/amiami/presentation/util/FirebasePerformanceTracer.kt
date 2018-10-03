package ru.cherryperry.amiami.presentation.util

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import ru.cherryperry.amiami.domain.performance.PerformanceTracer
import javax.inject.Inject

/**
 * [PerformanceTracer] implementation with [FirebasePerformance].
 */
class FirebasePerformanceTracer @Inject constructor() : PerformanceTracer {

    private var trace: Trace? = null

    override fun startTrace(name: String, attributes: Map<String, String>): PerformanceTracer {
        if (trace != null) {
            throw IllegalStateException("Call stopTrace first!")
        }
        trace = FirebasePerformance.getInstance().newTrace(name).apply {
            attributes.forEach { putAttribute(it.key, it.value) }
            start()
        }
        return this
    }

    override fun stopTrace() {
        trace.let {
            if (it == null) {
                throw IllegalStateException("Call startTrace first!")
            }
            it.stop()
            trace = null
        }
    }
}
