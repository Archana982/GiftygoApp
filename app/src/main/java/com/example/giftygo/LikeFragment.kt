package com.example.giftygo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftygo.Extensions.toast
import com.example.giftygo.Models.LikeModel
import com.example.giftygo.databinding.FragmentLikepageBinding
import com.example.giftygo.rvadapters.LikeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LikeFragment : Fragment(R.layout.fragment_likepage) {

    private lateinit var binding: FragmentLikepageBinding
    private lateinit var adapter: LikeAdapter
    private lateinit var likedProductList: MutableList<LikeModel>
    private val likeDBRef = Firebase.firestore.collection("LikedProducts")
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLikepageBinding.bind(view)
        likedProductList = mutableListOf()

        binding.rvLikedProducts.layoutManager = LinearLayoutManager(requireContext())

        val userId = auth.currentUser?.uid

        if (userId != null) {
            likeDBRef.whereEqualTo("uid", userId).get()
                .addOnSuccessListener { docs ->
                    likedProductList.clear()
                    for (doc in docs) {
                        val item = doc.toObject(LikeModel::class.java)
                        likedProductList.add(item)
                    }

                    adapter = LikeAdapter(
                        likedProductList,
                        onClickLike = { item -> onClickLike(item) },
                        onItemClick = { item -> navigateToDetails(item) }
                    )

                    binding.rvLikedProducts.adapter = adapter
                }
                .addOnFailureListener {
                    requireActivity().toast("Failed to load liked products")
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickLike(item: LikeModel) {
        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("pid", item.pid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot) {
                    likeDBRef.document(doc.id).delete()
                    likedProductList.remove(item)
                    adapter.notifyDataSetChanged()
                    requireActivity().toast("Removed from the Liked Items")
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Failed to remove from Liked Items")
            }
    }

    private fun navigateToDetails(item: LikeModel) {
        val bundle = Bundle().apply {
            putString("pid", item.pid)
            putString("name", item.name)
            putString("description", item.description)
            putString("color", item.color)
            putString("glbUrl", item.glbUrl)
            putInt("price", item.price)
        }

        findNavController().navigate(R.id.detailsFragment, bundle)

    }
}