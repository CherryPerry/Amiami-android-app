plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(project.ext["compileSdkVersion"] as Int)
    buildToolsVersion(project.ext["buildToolsVersion"] as String)

    defaultConfig {
        minSdkVersion(project.ext["minSdkVersion"] as Int)
        targetSdkVersion(project.ext["targetSdkVersion"] as Int)
    }

    buildTypes {
        getByName("release") {
            consumerProguardFile("proguard-rules.pro")
        }
    }
}

dependencies {
    api(project(":core"))

    api("org.jetbrains.kotlin:kotlin-stdlib:${ext["kotlinVersion"]}")

    kapt("com.google.dagger:dagger-compiler:${ext["daggerVersion"]}")

    testImplementation("junit:junit:${ext["junitVersion"]}")
    testImplementation("org.mockito:mockito-core:2.21.0")
}
