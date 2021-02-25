package ru.cherryperry.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

abstract class BasePlugin {

    protected fun setupDefaultParams(project: Project) {
        project.extensions.configure(BaseExtension::class.java) { extension ->
            extension.compileSdkVersion(28)
            extension.buildToolsVersion("28.0.3")
            extension.defaultConfig.run {
                minSdkVersion(16)
                targetSdkVersion(28)
            }
            extension.buildTypes.named("release") {
                it.consumerProguardFile("proguard-rules.pro")
            }
            extension.compileOptions.run {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

}
