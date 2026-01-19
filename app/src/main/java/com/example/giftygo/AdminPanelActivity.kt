package com.example.giftygo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftygo.Models.OrderModel
import com.example.giftygo.databinding.ActivityAdminPanelBinding
import com.example.giftygo.rvadapters.OrderAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private lateinit var orderAdapter: OrderAdapter
    private val orderList = mutableListOf<OrderModel>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(orderList,
            onAcceptClick = { order -> updateOrderStatus(order, "Accepted") },
            onPreparedClick = { order -> updateOrderStatus(order, "Prepared") }
        )
        binding.ordersRecyclerView.adapter = orderAdapter

        loadOrders()
    }

    private fun loadOrders() {
        db.collection("Orders")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                val orders = querySnapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)?.apply { documentId = doc.id }
                } ?: emptyList()
                orderAdapter.updateOrderList(orders)
            }
    }

    private fun updateOrderStatus(order: OrderModel, newStatus: String) {
        if (newStatus == "Prepared") {
            // Delete the order when prepared
            db.collection("Orders").document(order.documentId!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Order removed after preparation", Toast.LENGTH_SHORT).show()

                    val message =
                        "Your order '${order.productName}' is prepared. Please come to CMLI to collect."
                    sendWhatsAppMessage(order.customerPhone ?: "", message)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to delete order", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Update status for other actions (e.g., Accepted)
            db.collection("Orders").document(order.documentId!!)
                .update("orderStatus", newStatus)
                .addOnSuccessListener {
                    Toast.makeText(this, "Order $newStatus", Toast.LENGTH_SHORT).show()

                    val message = when (newStatus) {
                        "Accepted" -> "✅ Your order '${order.productName}' has been accepted!"
                        else -> "Your order status: $newStatus"
                    }

                    sendWhatsAppMessage(order.customerPhone ?: "", message)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update order", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun sendWhatsAppMessage(phoneNumber: String, message: String) {
        try {
            val cleanNumber = phoneNumber.replace("+", "").trim()
            val url = "https://wa.me/$cleanNumber?text=${Uri.encode(message)}"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                setPackage("com.whatsapp")
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
