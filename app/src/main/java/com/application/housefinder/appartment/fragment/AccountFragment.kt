package com.application.housefinder.appartment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.LoginActivity
import com.application.housefinder.appartment.RegisterActivity
import com.application.housefinder.appartment.databinding.FragmentAccountBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.apache.commons.validator.routines.EmailValidator


class AccountFragment(var parent : RegisterActivity) : Fragment(){

    lateinit var binding : FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNext.setOnClickListener {
            val txtUserName = binding.edtUsername.text.toString()
            val txtEmail = binding.edtEmail.text.toString()
            val txtPassword = binding.edtPassword.text.toString()
            val txtReEnterPassword = binding.edtReEnterPassword.text.toString()

            if (txtUserName.isEmpty() || txtEmail.isEmpty() || txtPassword.isEmpty() || txtReEnterPassword.isEmpty()) {
                Toast.makeText(parent,"All field is required!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (!EmailValidator.getInstance().isValid(txtEmail)) {
                Toast.makeText(parent,"Invalid Email Format",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!LoginActivity.checkValidUsername(txtUserName)) {
                Toast.makeText(parent,"username must not contain '.', '#', '\$', '[', or ']'",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (txtPassword != txtReEnterPassword) {
                Toast.makeText(parent,"Password not match",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            parent.dataReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(txtUserName)) {
                        Toast.makeText(parent,"Username is used",Toast.LENGTH_SHORT).show()
                        return
                    }
                    parent.userName = txtUserName
                    parent.email = txtEmail
                    parent.password = txtPassword

                    parent.binding.vp.currentItem = 1

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(parent,"Something error" + error.message,Toast.LENGTH_SHORT).show()
                }

            })


        }

    }



}