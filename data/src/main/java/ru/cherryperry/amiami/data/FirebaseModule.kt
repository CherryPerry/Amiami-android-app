package ru.cherryperry.amiami.data

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun firebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}
