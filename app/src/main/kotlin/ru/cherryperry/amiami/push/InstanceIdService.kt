package ru.cherryperry.amiami.push

import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
    }
}
