package com.application.housefinder.appartment.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.NewPostActivity
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.databinding.FragmentFeedBinding
import com.application.housefinder.appartment.unit.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedFragment : Fragment() {
    lateinit var binding: FragmentFeedBinding
    lateinit var postAdapter: PostAdapter
    var postList = ArrayList<Post>()

    private val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataReference.child("posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                collectPostData(snapshot.children)
                postAdapter = PostAdapter(requireActivity(), postList)
                binding.rcvData.layoutManager = LinearLayoutManager(requireActivity())
                binding.rcvData.adapter = postAdapter
                Handler().postDelayed({ binding.rcvData.hideShimmerAdapter() }, 2000)
            }

            override fun onCancelled(error: DatabaseError) {
                val alert = AlertDialog.Builder(requireActivity())
                alert.setTitle("Failed to get data")
                alert.setMessage("Something went wrong (You may be check your connection)")
//                alert.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
//
//                }
                alert.create()
                Handler().postDelayed({ binding.rcvData.hideShimmerAdapter() }, 2000)
            }

        })

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(requireActivity(),NewPostActivity::class.java))
        }

    }

    private fun collectPostData(data: Iterable<DataSnapshot>) {
        for (child in data) {

            var id = child.child("id").getValue(String::class.java)?.toLong()!!
            val owner = child.child("owner").getValue(String::class.java)!!
            val title = child.child("title").getValue(String::class.java)!!
            val content = child.child("content").getValue(String::class.java)!!
            var dataImgList = ArrayList<String>()
            for (img in child.child("imgs").children) {
                img.child("path").getValue(String::class.java)?.let {
                    dataImgList.add(it)
                }
            }

            val newPost = Post(id, owner, title, content, dataImgList.toList())
            postList.add(newPost)
        }
        postList.reverse()
    }


}