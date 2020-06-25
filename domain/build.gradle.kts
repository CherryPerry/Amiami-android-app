/** Domain */

plugins {
    id("library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("detekt")
}

dependencies {
    api(project(":core"))

    api("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlinVersion")}")

    kapt("com.google.dagger:dagger-compiler:${property("daggerVersion")}")

    testImplementation("junit:junit:${property("junitVersion")}")
    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
}
