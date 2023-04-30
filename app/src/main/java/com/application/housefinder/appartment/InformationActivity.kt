package com.application.housefinder.appartment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.application.housefinder.appartment.databinding.ActivityInformationBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InformationActivity : AppCompatActivity() {
    lateinit var binding: ActivityInformationBinding
    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    var username = Application.username
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this@InformationActivity, EditInfoActivity::class.java))
        }

        if (intent.getBooleanExtra("fromPost", false)) {
            username = intent.getStringExtra("username")!!
            binding.btnChat.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
        }
        binding.tvUsername.text = username

        binding.btnChat.setOnClickListener {
            var exist = false
            dataReference.child("chatroom")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val case1 = ":!" + Application.username + ":!" + username + ":!"
                        val case2 = ":!" + username + ":!" + Application.username + ":!"

                        if (snapshot.hasChild(case1)) {
                            startActivity(
                                Intent(this@InformationActivity, RoomChatActivity::class.java)
                                    .putExtra("room_name", case1)
                                    .putExtra("last_message", 1L)
                            )
                        } else if (snapshot.hasChild(case2)) {
                            startActivity(
                                Intent(this@InformationActivity, RoomChatActivity::class.java)
                                    .putExtra("room_name", case2)
                                    .putExtra("last_message", 1L)
                            )
                        } else {
                            startActivity(
                                Intent(this@InformationActivity, RoomChatActivity::class.java)
                                    .putExtra("room_name", case1)
                                    .putExtra("last_message", 0L));
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }
    }

    override fun onResume() {
        super.onResume()
        dataReference.child("users").child(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvFullName.text =
                        snapshot.child("fullname").getValue(String::class.java)
                    binding.tvAddress.text = snapshot.child("address").getValue(String::class.java)
                    binding.tvPhoneNumber.text =
                        snapshot.child("phonenumber").getValue(String::class.java)
                    binding.tvEmail.text = snapshot.child("email").getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    val alert = AlertDialog.Builder(this@InformationActivity)
                    alert.setTitle("Failed to get data")
                    alert.setMessage("Something went wrong (You may be check your connection) " + error.message)
                    alert.setNeutralButton(
                        "Exit"
                    ) { _, _ -> finish() }
                    alert.create()
                }

            })
    }
}