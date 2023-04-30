package com.application.housefinder.appartment.unit

import com.application.housefinder.appartment.Application

class RoomChat(val roomName : String, val messages : ArrayList<Message>) {
    companion object {
        fun splitOtherName(roomName : String) : String {
           val listName = roomName.split(":!")
            for (name in listName) {
                if (name.isNotEmpty() && name != Application.username) {
                    return name
                }
            }
            return ""
        }
    }
}