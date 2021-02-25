package ru.cherryperry.android

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : BasePlugin(), Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("com.android.library")
        setupDefaultParams(target)
    }

}
