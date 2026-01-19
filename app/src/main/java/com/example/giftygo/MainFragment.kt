package com.example.giftygo

import android.annotation.SuppressLint
import android.os.Bundle


import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftygo.Extensions.toast
import com.example.giftygo.Models.LikeModel
import com.example.giftygo.Models.ShoeDisplayModel
import com.example.giftygo.databinding.FragmentMainpageBinding
import com.example.giftygo.rvadapters.CategoryOnClickInterface
import com.example.giftygo.rvadapters.LikeOnClickInterface
import com.example.giftygo.rvadapters.MainCategoryAdapter
import com.example.giftygo.rvadapters.ProductOnClickInterface
import com.example.giftygo.rvadapters.ShoeDisplayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.navigation.findNavController

class MainFragment : Fragment(R.layout.fragment_mainpage),
    CategoryOnClickInterface,
    ProductOnClickInterface, LikeOnClickInterface {

    private lateinit var binding: FragmentMainpageBinding
    private lateinit var productList: ArrayList<ShoeDisplayModel>
    private lateinit var categoryList: ArrayList<String>
    private lateinit var productsAdapter: ShoeDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainpageBinding.bind(view)
        categoryList = ArrayList()
        productList = ArrayList()

        auth = FirebaseAuth.getInstance()

        // region implements category Recycler view

        categoryList.add("Trending")
        binding.rvMainCategories.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMainCategories.layoutManager = categoryLayoutManager
        categoryAdapter = MainCategoryAdapter(categoryList, this)
        binding.rvMainCategories.adapter = categoryAdapter
        setCategoryList()

        // endregion implements category Recycler view

        // region implements products Recycler view

        val productLayoutManager = GridLayoutManager(context, 2)
        productsAdapter = ShoeDisplayAdapter(requireContext(), productList, this, this)
        binding.rvMainProductsList.layoutManager = productLayoutManager
        binding.rvMainProductsList.adapter = productsAdapter
        setProductsData()
        // endregion implements products Recycler view

        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    requireActivity().findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_self)
                    true
                }

                R.id.likeFragment -> {
                    requireActivity().findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_likeFragment2)
                    true
                }

                R.id.cartFragment -> {
                    requireActivity().findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_cartFragment)
                    true
                }

                R.id.profileFragment -> {
                    auth.signOut()
                    requireActivity().findNavController(R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_signInFragmentFragment)
                    true
                }

                else -> false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCategoryList() {
        val firestore = Firebase.firestore
        firestore.collection("glbModels")  // 🔥 use lowercase
            .get()
            .addOnSuccessListener { documents ->
                categoryList.clear()
                categoryList.add("Trending")
                val uniqueColors = mutableSetOf<String>()
                for (document in documents) {
                    val product = document.toObject(ShoeDisplayModel::class.java)
                    product.color?.let { uniqueColors.add(it) }
                }
                categoryList.addAll(uniqueColors)
                categoryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching categories: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setProductsData() {
        val firestore = Firebase.firestore
        firestore.collection("glbModels")
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    val product = document.toObject(ShoeDisplayModel::class.java)
                    // ✅ Only add if image is available
                    if (!product.imgUrl.isNullOrEmpty()) {
                        productList.add(product)
                    }
                }
                productsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching products: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text
        val selectedColor = button.text.toString()

        Firebase.firestore.collection("glbModels")
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    val product = document.toObject(ShoeDisplayModel::class.java)
                    if (!product.imgUrl.isNullOrEmpty() &&
                        (selectedColor == "Trending" || product.color == selectedColor)
                    ) {
                        productList.add(product)
                    }
                }
                productsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error filtering products: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    override fun onClickProduct(item: ShoeDisplayModel) {
        val bundle = Bundle().apply {
            putString("glbUrl", item.glbUrl)
            putString("name", item.name)
            putString("description", item.description)
            putInt("price", item.price)
            putString("color", item.color)
        }

        requireActivity().findNavController(R.id.fragmentContainerView)
            .navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
    }


    override fun onClickLike(item: ShoeDisplayModel) {
        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("pid", item.id)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Not already liked — add it
                    likeDBRef.add(
                        LikeModel(
                            item.id,
                            auth.currentUser!!.uid,
                            item.color,
                            item.description,
                            item.imgUrl,
                            item.name,
                            item.price.toString()
                        )
                    ).addOnSuccessListener {
                        requireActivity().toast("Added to Liked Items")
                    }.addOnFailureListener {
                        requireActivity().toast("Failed to Add to Liked")
                    }
                } else {
                    requireActivity().toast("Already in Liked Items")
                }
            }
    }
}
