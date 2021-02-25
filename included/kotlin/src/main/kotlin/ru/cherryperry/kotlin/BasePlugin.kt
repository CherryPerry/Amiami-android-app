package ru.cherryperry.kotlin

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class BasePlugin {

    protected fun setupJavaVersion(project: Project) {
        project.tasks.withType(KotlinCompile::class.java).configureEach {
            it.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

}
