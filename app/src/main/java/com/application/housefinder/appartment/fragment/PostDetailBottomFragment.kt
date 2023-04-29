package com.application.housefinder.appartment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.housefinder.appartment.Application
import com.application.housefinder.appartment.R
import com.application.housefinder.appartment.adapter.PostAdapter
import com.application.housefinder.appartment.adapter.PostImageAdapter
import com.application.housefinder.appartment.databinding.FragmentPostDetailBinding
import com.application.housefinder.appartment.unit.Post
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class PostDetailBottomFragment(val post: Post) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentPostDetailBinding
    lateinit var imgAdapter: PostImageAdapter
    var fav = false

    val dataReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://appartment-finder-a1907-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = post.title
        binding.tvContent.text = post.description
        binding.tvPrice.text = post.price.toString() + "$"
        binding.tvLocation.text = post.address
        binding.tvNumber.text = post.personNumber.toString()

        imgAdapter = PostImageAdapter(
            requireActivity(),
            post.imgURLs,
            object : PostAdapter.PostClickCallback {
                override fun onPostClick(position: Int) {

                }
            })
        binding.save.isEnabled = false

        binding.rcvImg.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvImg.adapter = imgAdapter

        dataReference.child("users").child(Application.username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postId in snapshot.child("archives").children) {
                        val id = postId.child("child_id").getValue(String::class.java)?.toLong()
                        if (id == post.id) {
                            fav = true
                            binding.save.setImageResource(R.drawable.ic_baseline_bookmark_added_24)
                            binding.save.isEnabled = true
                            return
                        }
                    }
                    fav = false
                    binding.save.setImageResource(R.drawable.ic_bookmark)

                    binding.save.isEnabled = true

                }

                override fun onCancelled(error: DatabaseError) {
                    fav = false
                    binding.save.isEnabled = true
                    binding.save.setImageResource(R.drawable.ic_bookmark)

                    error.toException().printStackTrace()

                }

            })

        binding.save.setOnClickListener {
            dataReference.child("users").child(Application.username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (fav) {
                            removePostArchived()
                        } else {
                            addPostArchived()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException().printStackTrace()
                    }

                })
        }
    }

    private fun addPostArchived() {
        val postId = Calendar.getInstance().timeInMillis.toString()
        dataReference.child("users")
            .child(Application.username)
            .child("archives")
            .child(postId)
            .child("child_id").setValue(post.id.toString()).addOnCompleteListener {
                fav = true
                binding.save.setImageResource(R.drawable.ic_baseline_bookmark_added_24)

            }
    }

    private fun removePostArchived() {
        val ref = FirebaseDatabase.getInstance().reference
        val applesQuery = ref.child("users")
            .child(Application.username)
            .child("archives").orderByChild("child_id").equalTo(post.id.toString())

        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                }
                fav = false
                binding.save.setImageResource(R.drawable.ic_bookmark)

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}