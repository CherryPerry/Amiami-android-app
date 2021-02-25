package ru.cherryperry.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinPlugin : BasePlugin(), Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("kotlin")
        setupJavaVersion(target)
    }

}
