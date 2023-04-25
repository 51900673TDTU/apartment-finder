package com.application.housefinder.appartment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.application.housefinder.appartment.adapter.ImageAdapter
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.databinding.ActivityNewPostBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.Calendar


class NewPostActivity : AppCompatActivity() {
    lateinit var binding : ActivityNewPostBinding
    lateinit var imgAdapter : ImageAdapter
    var uriList = ArrayList<Uri>()

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    companion object {
        const val PICK_IMAGE = 2828
    }

    private fun fetchData() {
        uriList.add(Uri.EMPTY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchData()
        imgAdapter = ImageAdapter(this,uriList,object : PostAdapter.PostClickCallback {
            override fun onPostClick(position: Int) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            }

        })
        binding.rcvImg.layoutManager = GridLayoutManager(this,2)
        binding.rcvImg.adapter = imgAdapter

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.post.setOnClickListener {
            val txtTitle = binding.edtTitle.text.toString()
            val txtContent = binding.edtContent.text.toString()
            val txtLocation = binding.edtLocation.text.toString()
            val price = binding.edtPrice.text.toString()
            val personNumber = binding.edtNumber.text.toString()

            if (txtTitle.isEmpty()) {
                Toast.makeText(this@NewPostActivity,"Title required",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var priceInt = 0
            var personInt = 1
            if (price.isNotEmpty()) {
                priceInt = price.toInt()
            }
            if (personNumber.isNotEmpty()) {
                personInt = personNumber.toInt()
            }
            val postId = Calendar.getInstance().timeInMillis.toString()
            dataReference.child("posts").child(postId).child("owner").setValue(Application.username)
            dataReference.child("posts").child(postId).child("title").setValue(txtTitle)
            dataReference.child("posts").child(postId).child("content").setValue(txtContent)
            dataReference.child("posts").child(postId).child("id").setValue(postId)
            dataReference.child("posts").child(postId).child("price").setValue(priceInt)
            dataReference.child("posts").child(postId).child("persons").setValue(personInt)
            dataReference.child("posts").child(postId).child("location").setValue(txtLocation)
            updateImage(postId)

            finish()
        }
    }

    var storageRef = FirebaseStorage.getInstance().reference

    private fun updateImage(postId : String) {

        for (uri in uriList) {
            if (uri == Uri.EMPTY) {
                continue
            }
            val name = "img" + Calendar.getInstance().timeInMillis + ".jpg"
            val imgRef = storageRef.child(name)

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = imgRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.e("==debug", "updateImage: " + it.message )
                Toast.makeText(this@NewPostActivity,"Upload failed " + it.message,Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { it2 ->
                it2.storage.downloadUrl.addOnSuccessListener {
                    dataReference.child("posts")
                        .child(postId)
                        .child("imgs")
                        .child(Calendar.getInstance().timeInMillis.toString())
                        .child("path")
                        .setValue(it.toString())
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && data != null) {
            data.data?.let {
                uriList.add(it)
                imgAdapter.notifyDataSetChanged()
            }
        }
    }
}