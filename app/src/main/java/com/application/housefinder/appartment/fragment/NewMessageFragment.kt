package com.application.housefinder.appartment.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.application.housefinder.appartment.Application
import com.application.housefinder.appartment.RoomChatActivity
import com.application.housefinder.appartment.databinding.FragmentNewMessageBinding
import com.application.housefinder.appartment.unit.LiteMessage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class NewMessageFragment : BottomSheetDialogFragment(){
    lateinit var binding : FragmentNewMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMessageBinding.inflate(layoutInflater)
        return binding.root
    }
    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.send.setOnClickListener {

            val txtUsername = binding.edtUsername.text.toString()
            val msg = binding.edtMsg.text.toString()

            if (txtUsername.isEmpty() || msg.isEmpty()) {
                Toast.makeText(requireActivity(),"Username and message can't be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dataReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(txtUsername)) {
                        checkRoomExist(txtUsername, msg)
                    } else {
                        Toast.makeText(requireActivity(),"Username not found",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        }
    }

    private fun checkRoomExist(txtUsername: String, msg: String) {
        val date = Calendar.getInstance().timeInMillis
        val owner = Application.username

        dataReference.child("chatroom")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val case1 = ":!" + Application.username + ":!" + txtUsername + ":!"
                    val case2 = ":!" + txtUsername + ":!" + Application.username + ":!"

                    if (snapshot.hasChild(case1)) {
                        dataReference.child("chatroom").child(case1).child(date.toString())
                            .setValue(LiteMessage(owner, msg, false)).addOnCompleteListener {
                                this@NewMessageFragment.dismiss()
                            }
                    } else if (snapshot.hasChild(case2)) {
                        dataReference.child("chatroom").child(case2).child(date.toString())
                            .setValue(LiteMessage(owner, msg, false)).addOnCompleteListener {
                                this@NewMessageFragment.dismiss()
                            }
                    } else {
                        dataReference.child("chatroom").child(case1).child(date.toString())
                            .setValue(LiteMessage(owner, msg, false)).addOnCompleteListener {
                                this@NewMessageFragment.dismiss()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}