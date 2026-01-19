package com.example.giftygo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftygo.Models.OrderModel
import com.example.giftygo.databinding.FragmentCartpageBinding
import com.example.giftygo.rvadapters.CartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment(R.layout.fragment_cartpage) {

    private lateinit var binding: FragmentCartpageBinding
    private lateinit var cartAdapter: CartAdapter
    private val orderList = mutableListOf<OrderModel>()

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartpageBinding.bind(view)

        setupRecyclerView()
        listenToCartOrders()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            orderList,
            onOrderReceived = { orderId -> updateOrderStatus(orderId, "Received") },
            onOrderCancelled = { orderId -> updateOrderStatus(orderId, "Cancelled") }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun listenToCartOrders() {
        val user = auth.currentUser ?: return

        db.collection("Orders")
            .whereEqualTo("userId", user.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val updatedOrders = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)?.apply { orderId = doc.id }
                } ?: emptyList()

                cartAdapter.updateOrderList(updatedOrders)
            }
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        db.collection("Orders").document(orderId)
            .update("orderStatus", status)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Order marked as $status", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
