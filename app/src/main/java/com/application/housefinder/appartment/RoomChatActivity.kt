package com.application.housefinder.appartment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.adapter.MessageAdapter
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.databinding.ActivityRoomChatBinding
import com.application.housefinder.appartment.unit.LiteMessage
import com.application.housefinder.appartment.unit.Message
import com.application.housefinder.appartment.unit.RoomChat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class RoomChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityRoomChatBinding
    lateinit var msgAdapter: MessageAdapter
    var msgList = ArrayList<Message>()
    var room = ""
    var lastMsg = 0L
    var fromNew = false

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        msgAdapter = MessageAdapter(this, msgList, object : PostAdapter.PostClickCallback {
            override fun onPostClick(position: Int) {

            }
        })

        if (intent.getStringExtra("room_name") != null) {
            room = intent.getStringExtra("room_name").toString()
            binding.room.text = RoomChat.splitOtherName(room)
            lastMsg = intent.getLongExtra("last_message", 0L)

            fromNew = lastMsg == 0L

        } else {
            val alert = AlertDialog.Builder(this@RoomChatActivity)
            alert.setTitle("Failed to get data")
            alert.setMessage("Something went wrong (You may be check your connection) ")
            alert.setNeutralButton(
                "Exit"
            ) { _, _ -> finish() }
            alert.create()
        }

        binding.rcvMsg.layoutManager = LinearLayoutManager(this)
        binding.rcvMsg.adapter = msgAdapter

//        fetchMessage()
        listenForNewMessage()

        binding.msgButton.setOnClickListener {
            if (binding.msgInput.text.toString().isNotEmpty()) {
                val date = Calendar.getInstance().timeInMillis
                val owner = Application.username
                val msg = binding.msgInput.text.toString()

                dataReference.child("chatroom").child(room).child(date.toString())
                    .setValue(LiteMessage(owner, msg, false)).addOnCompleteListener {
                        binding.msgInput.text.clear()
                        if (fromNew) {
                            fromNew = false
                            listenForNewMessage()
                        }
                    }

//
//                dataReference.child("chatroom").child(room).child(date.toString()).child("message")
//                    .setValue(msg).addOnCompleteListener {
//                        binding.msgInput.text.clear()
//                    }

            }
        }
    }

    private fun listenForNewMessage() {
        if (!fromNew) {
            dataReference.child("chatroom").child(room)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val date = snapshot.ref.key!!
                        val owner = snapshot.child("owner").getValue(String::class.java)!!
                        val message = snapshot.child("message").getValue(String::class.java)!!
                        val seen = snapshot.child("seen").getValue(Boolean::class.java)!!
                        if (!seen && owner != Application.username) {
                            dataReference.child("chatroom").child(room).child(date).child("seen")
                                .setValue(true)
                            Log.e("==logseen=", "onChildAdded: set seen"  )
                        }

                        val msg = Message(date.toLong(), owner, message, true)
                        msgList.add(msg)

                        val newPosition = msgList.size - 1
                        msgAdapter.notifyItemInserted(newPosition)
                        binding.rcvMsg.scrollToPosition(newPosition)
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}