package com.application.housefinder.appartment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.housefinder.appartment.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvForgotPass.setOnClickListener {
//        AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity.class);
            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("abc@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "forgot password" + "{username}")
            emailIntent.selector = selectorIntent
            startActivityForResult(
                Intent.createChooser(emailIntent, "Send email to us by the email you registered to get your password..."),
                1234
            )
        }

        binding.tvSignIn.setOnClickListener {
            val txtAccount = binding.edtEmail.text.toString()
            val txtPassword = binding.edtPassword.text.toString()
            if (txtAccount.isNotEmpty() && txtPassword.isNotEmpty()) {
                if (checkValidUsername(txtAccount)) {
                    dataReference.child("users")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.hasChild(txtAccount)) {
                                    if (snapshot.child(txtAccount).child("password")
                                            .getValue(String::class.java).equals(txtPassword)
                                    ) {
                                        Application.username = txtAccount
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                        finish()

                                        Toast.makeText(
                                            this@LoginActivity, "Welcome "
                                                    + snapshot.child(txtAccount).child("fullname")
                                                .getValue(String::class.java), Toast.LENGTH_SHORT
                                        ).show()

                                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                                        prefs.edit().putString("username",txtAccount).apply()
                                        prefs.edit().putString("password",txtPassword).apply()


                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Wrong password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.edtPassword.text.clear()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Account not exist",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Server error $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid username",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    companion object {
        fun checkValidUsername(username: String): Boolean {
            return !username.contains("$")
                    && !username.contains("#")
                    && !username.contains(".")
                    && !username.contains("[")
                    && !username.contains("]")

        }
    }

}