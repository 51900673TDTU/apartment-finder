package com.application.housefinder.appartment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.application.housefinder.appartment.databinding.ActivityRegisterBinding
import com.application.housefinder.appartment.fragment.AccountFragment
import com.application.housefinder.appartment.fragment.InfoFragment
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")


    var userName = ""
    var email = ""
    var password = ""
    var fullName = ""
    var phoneNumber = ""
    var address = ""
    var type = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vp.adapter = VpAdapter(supportFragmentManager)
        binding.vp.currentItem = 0
    }

    inner class VpAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                return AccountFragment(this@RegisterActivity)
            }
            return InfoFragment(this@RegisterActivity)
        }

    }

    fun createAccount() {
        dataReference.child("users").child(userName).child("fullname").setValue(fullName)
        dataReference.child("users").child(userName).child("email").setValue(email)
        dataReference.child("users").child(userName).child("password").setValue(password)
        dataReference.child("users").child(userName).child("phonenumber").setValue(phoneNumber)
        dataReference.child("users").child(userName).child("address").setValue(address)
        dataReference.child("users").child(userName).child("type").setValue(type)

        Toast.makeText(this,"Create account successfully",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

}