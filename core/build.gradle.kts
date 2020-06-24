/** Core */

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("detekt")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlinVersion")}")

    api("com.google.dagger:dagger:${property("daggerVersion")}")

    api("io.reactivex.rxjava2:rxjava:${property("rxVersion")}")
    api("io.reactivex.rxjava2:rxandroid:2.1.0")
    api("io.reactivex.rxjava2:rxkotlin:2.3.0")
}
