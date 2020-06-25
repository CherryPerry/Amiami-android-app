/** Core */

plugins {
    id("library")
    id("kotlin-android")
    id("detekt")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlinVersion")}")

    api("com.google.dagger:dagger:${property("daggerVersion")}")

    api("io.reactivex.rxjava2:rxjava:${property("rxVersion")}")
    api("io.reactivex.rxjava2:rxandroid:2.1.0")
    api("io.reactivex.rxjava2:rxkotlin:2.3.0")
}
