package com.application.housefinder.appartment

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.application.housefinder.appartment.databinding.ActivityEditInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditInfoBinding

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvSubmit.setOnClickListener {
            val txtFullname = binding.edtFullname.text.toString()
            val txtAddress = binding.edtAdress.text.toString()
            val txtPhone = binding.edtPhonenumber.text.toString()
            val type = if (binding.rg.checkedRadioButtonId == binding.lease.id) "lease" else "rent"

            if (txtFullname.isEmpty() || txtAddress.isEmpty() || txtPhone.isEmpty()) {
                Toast.makeText(this@EditInfoActivity,"All fields can't be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dataReference.child("users").child(Application.username).child("fullname").setValue(txtFullname)
            dataReference.child("users").child(Application.username).child("phonenumber").setValue(txtPhone)
            dataReference.child("users").child(Application.username).child("address").setValue(txtAddress)
            dataReference.child("users").child(Application.username).child("type").setValue(type).addOnCompleteListener {
                Toast.makeText(this@EditInfoActivity,"Update successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        dataReference.child("users").child(Application.username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.edtFullname.setText( snapshot.child("fullname").getValue(String::class.java))
                    binding.edtAdress.setText(snapshot.child("address").getValue(String::class.java))
                    binding.edtPhonenumber.setText(snapshot.child("phonenumber").getValue(String::class.java))
                    if (snapshot.child("type").getValue(String::class.java).equals("lease")) {
                        binding.rg.check(binding.lease.id)
                    } else {
                        binding.rg.check(binding.rent.id)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    val alert = AlertDialog.Builder(this@EditInfoActivity)
                    alert.setTitle("Failed to get data")
                    alert.setMessage("Something went wrong (You may be check your connection) " + error.message)
                    alert.setNeutralButton("Exit"
                    ) { _, _ -> finish() }
                    alert.create()
                }

            })

    }
}