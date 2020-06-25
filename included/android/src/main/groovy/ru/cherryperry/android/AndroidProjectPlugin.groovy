package ru.cherryperry.android

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

import javax.annotation.Nonnull

class AndroidProjectPlugin extends BasePlugin implements Plugin<Project> {

    @Override
    void apply(@Nonnull Project target) {
        target.apply plugin: 'com.android.application'
        setupDefaultParams(target)
        target.extensions.configure(BaseAppModuleExtension) {
            signing(target, it)
            buildTypes(it)
            sourceSets(it)
        }
    }

    private void signing(Project project, BaseAppModuleExtension extension) {
        extension.signingConfigs.register('release') { config ->
            File keystorePropertiesFile = project.file('keystore.properties')
            if (!keystorePropertiesFile.exists() || !keystorePropertiesFile.canRead()) {
                project.logger.error('No keystore.properties file')
                return
            }
            Properties keystoreProperties = new Properties()
            keystorePropertiesFile.withInputStream { keystoreProperties.load(it) }

            config.storeFile project.file(keystoreProperties['storeFile'])
            config.storePassword keystoreProperties['storePassword']
            config.keyAlias keystoreProperties['keyAlias']
            config.keyPassword keystoreProperties['keyPassword']
        }
    }

    private void buildTypes(BaseAppModuleExtension extension) {
        extension.buildTypes {
            it.named('debug') {
                it.signingConfig extension.signingConfigs.release
                it.versionNameSuffix '.debug'
            }
            it.named('release') {
                it.signingConfig extension.signingConfigs.release
                it.minifyEnabled true
                it.shrinkResources true
                it.proguardFiles extension.getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            }
        }
    }

    private void sourceSets(BaseAppModuleExtension extension) {
        extension.sourceSets.named('main') { it.java.srcDir 'src/main/kotlin' }
        extension.sourceSets.named('test') { it.java.srcDir 'src/test/kotlin' }
        extension.sourceSets.named('androidTest') { it.java.srcDir 'src/androidTest/kotlin' }
    }
}
