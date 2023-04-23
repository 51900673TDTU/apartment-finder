package com.application.housefinder.appartment.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.RegisterActivity
import com.application.housefinder.appartment.databinding.FragmentInfoBinding
import org.apache.commons.validator.routines.EmailValidator


class InfoFragment(var parent : RegisterActivity) : Fragment() {
    lateinit var binding : FragmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCreate.setOnClickListener {
            val txtFullName = binding.edtFullname.text.toString()
            val txtAddress = binding.edtAdress.text.toString()
            val txtPhoneNumber = binding.edtPhonenumber.text.toString()
            val type = if (binding.rg.checkedRadioButtonId == binding.rent.id) "rent" else "lease"

            if (txtFullName.isEmpty() || txtAddress.isEmpty() || txtPhoneNumber.isEmpty()) {
                Toast.makeText(parent,"All field is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPhoneNo(txtPhoneNumber)) {
                Toast.makeText(parent,"Invalid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            parent.fullName = txtFullName
            parent.address = txtAddress
            parent.phoneNumber = txtPhoneNumber
            parent.type = type

            parent.createAccount()

        }
    }

    fun isValidPhoneNo(iPhoneNo: CharSequence?): Boolean {
        return !TextUtils.isEmpty(iPhoneNo) &&
                Patterns.PHONE.matcher(iPhoneNo).matches()
    }
}