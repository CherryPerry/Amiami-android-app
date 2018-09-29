plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(project.ext["compileSdkVersion"] as Int)

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
    api("org.jetbrains.kotlin:kotlin-stdlib:${ext["kotlinVersion"]}")

    api("com.google.dagger:dagger:${ext["daggerVersion"]}")

    api("io.reactivex.rxjava2:rxjava:${ext["rxVersion"]}")
    api("io.reactivex.rxjava2:rxandroid:2.1.0")
    api("io.reactivex.rxjava2:rxkotlin:2.3.0")
}
