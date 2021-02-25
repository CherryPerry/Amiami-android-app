package ru.cherryperry.android

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

class AndroidProjectPlugin : BasePlugin(), Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("com.android.application")
        setupDefaultParams(target)
        target.extensions.configure(BaseAppModuleExtension::class.java) {
            signing(target, it)
            buildTypes(it)
            sourceSets(it)
        }
    }

    private fun signing(project: Project, extension: BaseAppModuleExtension) {
        extension.signingConfigs.register("release") { config ->
            val keystorePropertiesFile = project.file("keystore.properties")
            if (!keystorePropertiesFile.exists() || !keystorePropertiesFile.canRead()) {
                project.logger.error("No keystore.properties file")
                return@register
            }
            val keystoreProperties = Properties()
            keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }

            config.storeFile = project.file(keystoreProperties["storeFile"])
            config.storePassword = keystoreProperties["storePassword"] as String?
            config.keyAlias = keystoreProperties["keyAlias"] as String?
            config.keyPassword = keystoreProperties["keyPassword"] as String?
        }
    }

    private fun buildTypes(extension: BaseAppModuleExtension) {
        extension.buildTypes.run {
            named("debug") { buildType ->
                buildType.signingConfig = extension.signingConfigs.getByName("release")
                buildType.versionNameSuffix = ".debug"
            }
            named("release") { buildType ->
                buildType.signingConfig = extension.signingConfigs.getByName("release")
                buildType.isMinifyEnabled = true
                buildType.isShrinkResources = true
                buildType.proguardFiles(extension.getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
    }

    private fun sourceSets(extension: BaseAppModuleExtension) {
        extension.sourceSets.run {
            named("main") { it.java.srcDir("src/main/kotlin") }
            named("test") { it.java.srcDir("src/test/kotlin") }
            named("androidTest") { it.java.srcDir("src/androidTest/kotlin") }
        }
    }

}
