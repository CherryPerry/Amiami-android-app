package ru.cherryperry.amiami.domain.performance

class EmptyPerformanceTracer : PerformanceTracer {

    override fun startTrace(name: String, attributes: Map<String, String>): PerformanceTracer = this

    override fun stopTrace() {
        // nothing
    }
}
