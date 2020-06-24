/** Domain */

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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
        //sourceCompatibility = JavaVersion.VERSION_1_8
        //targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    api(project(":core"))

    api("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlinVersion")}")

    kapt("com.google.dagger:dagger-compiler:${property("daggerVersion")}")

    testImplementation("junit:junit:${property("junitVersion")}")
    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
}
