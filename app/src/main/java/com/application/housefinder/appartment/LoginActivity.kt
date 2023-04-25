package com.application.housefinder.appartment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.application.housefinder.appartment.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding

    val dataReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataReference.child("post")

        binding.tvSignIn.setOnClickListener {
            val txtAccount = binding.edtEmail.text.toString()
            val txtPassword = binding.edtPassword.text.toString()
            if (txtAccount.isNotEmpty() && txtPassword.isNotEmpty()) {
                dataReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(txtAccount)) {
                            if (snapshot.child(txtAccount).child("password").getValue(String::class.java).equals(txtPassword)) {
                                Application.username = txtAccount
                                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                finish()

                                Toast.makeText(this@LoginActivity,"Welcome "
                                        + snapshot.child(txtAccount).child("fullname").getValue(String::class.java)
                                    , Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@LoginActivity,"Wrong password", Toast.LENGTH_SHORT).show()
                                binding.edtPassword.text.clear()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity,"Account not exist", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Server error $error", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }
}