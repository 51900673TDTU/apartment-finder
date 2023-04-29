package com.application.housefinder.appartment.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.MainActivity
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.databinding.ActivitySearchBinding
import com.application.housefinder.appartment.unit.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    lateinit var binding: ActivitySearchBinding
    lateinit var postAdapter: PostAdapter
    var postList = ArrayList<Post>()

    private val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter(requireActivity(), postList, object : PostAdapter.PostClickCallback {
            override fun onPostClick(position: Int) {
                val sheet = PostDetailBottomFragment(postList[position])
                sheet.show(requireActivity().supportFragmentManager, "post_fragment")
            }
        })

        binding.rcvPost.layoutManager = LinearLayoutManager(requireActivity())
        binding.rcvPost.adapter = postAdapter

        binding.btnSearch.setOnClickListener {
            postList.clear()
            val txtWord = binding.edtSearch.text.toString()
            val txtPrice = binding.edtPrice.text.toString()
            val txtLocation = binding.edtLocation.text.toString()
            val txtPerson = binding.edtNumber.text.toString()

            dataReference.child("posts").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        var id = child.child("id").getValue(String::class.java)?.toLong()!!
                        val owner = child.child("owner").getValue(String::class.java)!!
                        val title = child.child("title").getValue(String::class.java)!!
                        val content = child.child("content").getValue(String::class.java)!!
                        val price = child.child("price").getValue(Int::class.java)!!
                        val person = child.child("persons").getValue(Int::class.java)!!
                        val location = child.child("location").getValue(String::class.java)!!

                        val containsWord =
                            if (txtWord.isEmpty()) true else (
                                    owner.contains(txtWord) || title.contains(txtWord) || content.contains(txtWord))
                        val matchPerson =
                            if (txtPerson.isEmpty()) true else
                                person == txtPerson.toInt()
                        val matchPrice =
                            if (txtPrice.isEmpty()) true else
                                (price <= txtPrice.toInt())
                        val matchLocation =
                            if (txtLocation.isEmpty()) true else
                                location.contains(txtLocation)

                        if (containsWord && matchPerson && matchLocation && matchPrice) {
                            Log.e("==test=", "Price : getherer")

                            var dataImgList = ArrayList<String>()
                            for (img in child.child("imgs").children) {
                                img.child("path").getValue(String::class.java)?.let {
                                    dataImgList.add(it)
                                }
                            }

                            val newPost = Post(id, owner, title, content, dataImgList.toList(),location,price,person)
                            postList.add(newPost)
                        }

                    }
                    postList.reverse()
                    postAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    val alert = AlertDialog.Builder(requireActivity())
                    alert.setTitle("Failed to get data")
                    alert.setMessage("Something went wrong (You may be check your connection)")
                    alert.create()
                }

            })
        }

    }
}