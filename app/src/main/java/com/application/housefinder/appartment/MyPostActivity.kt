package com.application.housefinder.appartment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.databinding.ActivityMyPostBinding
import com.application.housefinder.appartment.fragment.PostDetailBottomFragment
import com.application.housefinder.appartment.unit.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPostBinding
    lateinit var postAdapter: PostAdapter
    var postList = ArrayList<Post>()
    var isArchives = false

    private val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postAdapter = PostAdapter(this, postList, object : PostAdapter.PostClickCallback {
            override fun onPostClick(position: Int) {
                val sheet = PostDetailBottomFragment(postList[position])
                sheet.show(supportFragmentManager, "post_fragment")
            }

        })

        binding.rcvPost.layoutManager = LinearLayoutManager(this)
        binding.rcvPost.adapter = postAdapter

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.rlArchive.setOnClickListener {
            if (isArchives) {
                binding.icMode.setImageResource(R.drawable.ic_bookmark)
                binding.tvMode.text = "Archived post"

                fetchMyPost()
                isArchives = false
            } else {
                binding.icMode.setImageResource(R.drawable.ic_add_post)
                binding.tvMode.text = "My post"
                fetchArchivedPost()
                isArchives = true
            }
        }
        fetchMyPost()
    }

    private fun fetchArchivedPost() {
        postList.clear()
        val postArchivedId = ArrayList<Long>()
        dataReference.child("users").child(Application.username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("archives")) {
                        for (child in snapshot.child("archives").children) {
                            postArchivedId.add(
                                child.child("child_id").getValue(String::class.java)?.toLong()!!
                            )
                        }

                        dataReference.child("posts")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (child2 in snapshot.children) {
                                        val id = child2.child("id").getValue(String::class.java)
                                            ?.toLong()!!
                                        if (postArchivedId.contains(id)) {
                                            val owner =
                                                child2.child("owner").getValue(String::class.java)!!
                                            val title =
                                                child2.child("title").getValue(String::class.java)!!
                                            val content = child2.child("content")
                                                .getValue(String::class.java)!!
                                            val price =
                                                child2.child("price").getValue(Int::class.java)!!
                                            val person =
                                                child2.child("persons").getValue(Int::class.java)!!
                                            val location = child2.child("location")
                                                .getValue(String::class.java)!!
                                            val dataImgList = ArrayList<String>()
                                            for (img in child2.child("imgs").children) {
                                                img.child("path").getValue(String::class.java)
                                                    ?.let {
                                                        dataImgList.add(it)
                                                    }
                                            }

                                            val newPost = Post(
                                                id,
                                                owner,
                                                title,
                                                content,
                                                dataImgList.toList(),
                                                location,
                                                price,
                                                person
                                            )
                                            postList.add(newPost)
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged()
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


    private fun fetchMyPost() {
        postList.clear()
        dataReference.child("posts").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val owner = child.child("owner").getValue(String::class.java)!!
                    if (owner == Application.username) {
                        val id = child.child("id").getValue(String::class.java)?.toLong()!!
                        val title = child.child("title").getValue(String::class.java)!!
                        val content = child.child("content").getValue(String::class.java)!!
                        val price = child.child("price").getValue(Int::class.java)!!
                        val person = child.child("persons").getValue(Int::class.java)!!
                        val location = child.child("location").getValue(String::class.java)!!
                        val dataImgList = ArrayList<String>()
                        for (img in child.child("imgs").children) {
                            img.child("path").getValue(String::class.java)?.let {
                                dataImgList.add(it)
                            }
                        }

                        val newPost = Post(
                            id,
                            owner,
                            title,
                            content,
                            dataImgList.toList(),
                            location,
                            price,
                            person
                        )
                        postList.add(newPost)
                    }

                }
                postList.reverse()
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val alert = AlertDialog.Builder(this@MyPostActivity)
                alert.setTitle("Failed to get data")
                alert.setMessage("Something went wrong (You may be check your connection) " + error.message)
                alert.create()
            }
        })

    }
}