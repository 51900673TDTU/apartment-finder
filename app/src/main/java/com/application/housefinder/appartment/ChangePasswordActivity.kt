package com.application.housefinder.appartment

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.application.housefinder.appartment.databinding.ActivityChangePasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvSubmit.setOnClickListener {
            val txtCurrent = binding.edtPassword.text.toString()
            val txtNew = binding.edtNewPassword.text.toString()
            val txtReNew = binding.edtReEnterPassword.text.toString()

            if (txtCurrent.isEmpty()) {
                Toast.makeText(this, "Enter current password!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txtNew.isEmpty()) {
                Toast.makeText(this, "Enter new password!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txtReNew.isEmpty()) {
                Toast.makeText(this, "Re - Enter new password!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txtReNew != txtNew) {
                Toast.makeText(this, "Re-entered password not match!!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dataReference.child("users").child(Application.username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val pas = snapshot.child("password").getValue(String::class.java)!!
                        if (pas == txtCurrent) {
                            dataReference.child("users").child(Application.username)
                                .child("password").setValue(txtNew).addOnCompleteListener {
                                    Toast.makeText(
                                        this@ChangePasswordActivity,
                                        "Change password successfully!!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                        } else {
                            Toast.makeText(this@ChangePasswordActivity, "Password not match!!!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        val alert = AlertDialog.Builder(this@ChangePasswordActivity)
                        alert.setTitle("Failed to get data")
                        alert.setMessage("Something went wrong (You may be check your connection) " + error.message)
                        alert.create()
                    }

                })


        }
    }
}