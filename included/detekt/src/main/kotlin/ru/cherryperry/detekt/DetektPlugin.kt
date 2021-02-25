package ru.cherryperry.detekt

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply { it.plugin("io.gitlab.arturbosch.detekt") }
        target.extensions.configure(DetektExtension::class.java) { extension ->
            extension.config.from(target.rootProject.file("included/detekt/detekt.yml"))
            extension.baseline = target.file("detekt-baseline.xml")
        }
        target.tasks.withType(Detekt::class.java).configureEach { task ->
            task.jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
        target.dependencies.add(
            "detektPlugins",
            "io.gitlab.arturbosch.detekt:detekt-formatting:1.9.1"
        )
    }
}
