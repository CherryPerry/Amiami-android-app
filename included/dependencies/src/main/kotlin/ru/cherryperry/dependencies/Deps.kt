package ru.cherryperry.dependencies

@Suppress("ClassName", "unused")
object Deps {
    object dagger {
        private const val version = "2.32"
        const val library = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val androidAnnotationProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object glide {
        private const val version = "4.8.0"
        const val library = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object retrofit {
        private const val version = "2.4.0"
        const val library = "com.squareup.retrofit2:retrofit:$version"
        const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
        const val converterScalars = "com.squareup.retrofit2:converter-scalars:$version"
        const val adapterRxjava2 = "com.squareup.retrofit2:adapter-rxjava2:$version"
    }

    object rxjava2 {
        const val library = "io.reactivex.rxjava2:rxjava:2.2.1"
        const val android = "io.reactivex.rxjava2:rxandroid:2.1.0"
        const val kotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
    }

    object junit {
        const val library = "junit:junit:4.12"
    }

    object okhttp {
        private const val version = "3.11.0"
        const val library = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object robolectric {
        const val library = "org.robolectric:robolectric:3.8"
    }

    object mockito {
        const val library = "org.mockito:mockito-core:2.22.0"
    }

    object firebase {
        const val core = "com.google.firebase:firebase-core:16.0.4"
        const val messaging = "com.google.firebase:firebase-messaging:17.3.3"
        const val perf = "com.google.firebase:firebase-perf:16.1.2"
        const val plugins = "com.google.firebase:firebase-plugins:1.1.5"
    }

    object room {
        private const val version = "2.2.6"
        const val runtime = "android.arch.persistence.room:runtime:$version"
        const val rxjava2 = "android.arch.persistence.room:rxjava2:$version"
        const val compiler = "android.arch.persistence.room:compiler:$version"
    }

    object androidx {
        const val core = "androidx.core:core-ktx:1.0.0"
        const val browser = "androidx.browser:browser:1.2.0"

        object lifecycle {
            private const val version = "1.1.1"
            const val extensions = "android.arch.lifecycle:extensions:1.1.1"
            const val compiler = "android.arch.lifecycle:compiler:1.1.1"
        }
    }
}
