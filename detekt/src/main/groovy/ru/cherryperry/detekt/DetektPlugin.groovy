package ru.cherryperry.detekt

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

import javax.annotation.Nonnull

class DetektPlugin implements Plugin<Project> {

    @Override
    void apply(@Nonnull Project target) {
        target.apply plugin: 'io.gitlab.arturbosch.detekt'
        target.extensions.configure(DetektExtension) {
            it.config.from(target.rootProject.file('detekt/detekt.yml'))
            it.baseline = target.file('detekt-baseline.xml')
        }
        target.tasks.withType(Detekt).configureEach {
            it.jvmTarget = JavaVersion.VERSION_1_8
        }
        target.dependencies.add(
            'detektPlugins',
            'io.gitlab.arturbosch.detekt:detekt-formatting:1.9.1'
        )
    }
}
