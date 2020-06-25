/** Data */

plugins {
    id("library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("detekt")
}

dependencies {
    implementation(project(":domain"))

    kapt("com.google.dagger:dagger-compiler:${property("daggerVersion")}")

    implementation("androidx.core:core-ktx:1.0.0")

    implementation("com.squareup.okhttp3:okhttp:${property("okhttpVersion")}")
    implementation("com.squareup.okhttp3:logging-interceptor:${property("okhttpVersion")}")

    implementation("com.squareup.retrofit2:retrofit:${property("retoriftVersion")}")
    implementation("com.squareup.retrofit2:converter-gson:${property("retoriftVersion")}")
    implementation("com.squareup.retrofit2:converter-scalars:${property("retoriftVersion")}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${property("retoriftVersion")}")

    implementation("com.google.firebase:firebase-core:${property("firebaseCoreVersion")}")
    implementation("com.google.firebase:firebase-messaging:${property("firebaseMessagingVersion")}")
    implementation("com.android.support:support-v4:${property("compatVersion")}")

    // TODO Why??
    api("android.arch.persistence.room:runtime:1.1.1")
    implementation("android.arch.persistence.room:rxjava2:1.1.1")
    kapt("android.arch.persistence.room:compiler:1.1.1")

    testImplementation("junit:junit:${property("junitVersion")}")
    testImplementation("org.robolectric:robolectric:${property("robolectricVersion")}")
    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
}
