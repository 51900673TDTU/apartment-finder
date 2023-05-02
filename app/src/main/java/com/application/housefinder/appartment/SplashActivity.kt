package com.application.housefinder.appartment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val txtAccount = sharedPreferences.getString("username","")!!
        val txtPassword = sharedPreferences.getString("password","")
        val firstOpen = sharedPreferences.getBoolean("first_open",true)

        if (!firstOpen) {
            if (txtAccount.isEmpty()) {
                Handler().postDelayed({
                    startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                    finish()
                },5000)
            } else {
                dataReference.child("users")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild(txtAccount)) {
                                if (snapshot.child(txtAccount).child("password")
                                        .getValue(String::class.java).equals(txtPassword)) {

                                    Application.username = txtAccount
                                    startActivity(
                                        Intent(
                                            this@SplashActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()

                                    Toast.makeText(
                                        this@SplashActivity, "Welcome "
                                                + snapshot.child(txtAccount).child("fullname")
                                            .getValue(String::class.java), Toast.LENGTH_SHORT
                                    ).show()



                                } else {
                                    val prefs = PreferenceManager.getDefaultSharedPreferences(this@SplashActivity)
                                    prefs.edit().putString("username","").apply()
                                    prefs.edit().putString("password","").apply()

                                    startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@SplashActivity,
                                    "Account not exist",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@SplashActivity,
                                "Server error $error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            }
        } else {
            sharedPreferences.edit().putBoolean("first_open",false).apply()
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        }


    }
}