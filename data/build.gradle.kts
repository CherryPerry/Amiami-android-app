/** Data */

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

    buildTypes.getByName("release").consumerProguardFile("proguard-rules.pro")

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(project(":domain"))

    kapt("com.google.dagger:dagger-compiler:${ext["daggerVersion"]}")

    implementation("androidx.core:core-ktx:0.3")

    implementation("com.squareup.okhttp3:okhttp:${ext["okhttpVersion"]}")
    implementation("com.squareup.okhttp3:logging-interceptor:${ext["okhttpVersion"]}")

    implementation("com.squareup.retrofit2:retrofit:${ext["retoriftVersion"]}")
    implementation("com.squareup.retrofit2:converter-gson:${ext["retoriftVersion"]}")
    implementation("com.squareup.retrofit2:converter-scalars:${ext["retoriftVersion"]}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${ext["retoriftVersion"]}")

    implementation("com.google.firebase:firebase-core:${ext["firebaseCoreVersion"]}")
    implementation("com.google.firebase:firebase-messaging:${ext["firebaseMessagingVersion"]}")
    implementation("com.android.support:support-v4:${ext["compatVersion"]}")

    // TODO Why??
    api("android.arch.persistence.room:runtime:1.1.1")
    implementation("android.arch.persistence.room:rxjava2:1.1.1")
    kapt("android.arch.persistence.room:compiler:1.1.1")

    testImplementation("junit:junit:${ext["junitVersion"]}")
    testImplementation("org.robolectric:robolectric:${ext["robolectricVersion"]}")
    testImplementation("org.mockito:mockito-core:${ext["mockitoVersion"]}")
}
