package com.application.housefinder.appartment

import com.google.firebase.FirebaseApp

class Application : android.app.Application() {
    companion object {
        var username = ""
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this);

    }
}