package ru.cherryperry.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

import javax.annotation.Nonnull

abstract class BasePlugin {

    protected void setupDefaultParams(@Nonnull Project project) {
        project.extensions.configure(BaseExtension) {
            it.compileSdkVersion 28
            it.buildToolsVersion '28.0.3'
            it.defaultConfig {
                it.minSdkVersion 16
                it.targetSdkVersion 28
            }
            it.buildTypes.named('release') {
                it.consumerProguardFile('proguard-rules.pro')
            }
            it.compileOptions {
                it.sourceCompatibility = JavaVersion.VERSION_1_8
                it.targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

}