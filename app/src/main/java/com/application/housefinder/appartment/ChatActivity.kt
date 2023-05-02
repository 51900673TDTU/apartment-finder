package com.application.housefinder.appartment

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.adapter.RoomChatAdapter
import com.application.housefinder.appartment.databinding.ActivityChatBinding
import com.application.housefinder.appartment.fragment.NewMessageFragment
import com.application.housefinder.appartment.fragment.PostDetailBottomFragment
import com.application.housefinder.appartment.unit.Message
import com.application.housefinder.appartment.unit.RoomChat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var msgAdapter: RoomChatAdapter
    var roomList = ArrayList<RoomChat>()

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        msgAdapter = RoomChatAdapter(this, roomList, object : PostAdapter.PostClickCallback {
            override fun onPostClick(position: Int) {
                startActivity(
                    Intent(
                        this@ChatActivity,
                        RoomChatActivity::class.java
                    ).putExtra("room_name", roomList[position].roomName)
                        .putExtra(
                            "last_message",
                            roomList[position].messages[roomList[position].messages.size - 1].date
                        )
                )
            }
        })

        binding.rcvMessage.layoutManager = LinearLayoutManager(this)
        binding.rcvMessage.adapter = msgAdapter

        binding.btnChat.setOnClickListener {
            val sheet = NewMessageFragment()
            sheet.show(supportFragmentManager, "chat_fragment")
        }

    }

    override fun onResume() {
        super.onResume()
        fetchRoomChat()
    }

    private fun fetchRoomChat() {
        dataReference.child("chatroom").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                //collect all room chat
                for (roomChild in snapshot.children) {
                    val roomName = roomChild.ref.key!!

                    //check if room contains username
                    if (roomName.contains(":!" + Application.username + ":!")) {
                        val newMsgList = ArrayList<Message>()

                        //collect message in room
                        for (msgChild in roomChild.children) {
                            val date = msgChild.ref.key!!
                            val owner = msgChild.child("owner").getValue(String::class.java)!!
                            val message = msgChild.child("message").getValue(String::class.java)!!
                            val seen = msgChild.child("seen").getValue(Boolean::class.java)!!
                            val newMsg = Message(date.toLong(), owner, message, seen)
                            newMsgList.add(newMsg)

                        }

                        val newRoom = RoomChat(roomName, newMsgList)
                        roomList.add(newRoom)
                    }
                }
                roomList.reverse()
                msgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val alert = AlertDialog.Builder(this@ChatActivity)
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