@file:Suppress("DEPRECATION")
package com.example.giftygo

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.giftygo.databinding.FragmentOrderSummaryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class OrderSummaryFragment : Fragment(R.layout.fragment_order_summary) {

  private lateinit var binding: FragmentOrderSummaryBinding
  private val auth = FirebaseAuth.getInstance()
  private val db = FirebaseFirestore.getInstance()
  private val args: OrderSummaryFragmentArgs by navArgs()

  private val FCM_SERVER_KEY = "4c77823f0be9b0a7e05db4583141a0e33695b203"
  private val FCM_API_URL = "https://fcm.googleapis.com/fcm/send"

  private var totalPrice: Int = 0

  override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentOrderSummaryBinding.bind(view)

    setupUI()
    setupListeners()
  }

  /** Setup UI with product details */
  private fun setupUI() {
    binding.tvProductName.text = args.name
    binding.tvProductColor.text = args.color
    binding.tvProductQuantity.text = args.quantity.toString()
    totalPrice = args.price * args.quantity
    binding.tvProductPrice.text = totalPrice.toString()
  }

  /** Setup button click listeners */
  private fun setupListeners() {
    binding.btnPlaceFinalOrder.setOnClickListener {
      placeOrder()
    }
  }

  /** Save order to Firestore */
  private fun placeOrder() {
    val name = binding.etCustomerName.text.toString().trim()
    val phone = binding.etCustomerPhone.text.toString().trim()

    if (name.isEmpty() || phone.isEmpty()) {
      Toast.makeText(requireContext(), "Enter customer details", Toast.LENGTH_SHORT).show()
      return
    }

    // ✅ Mobile format validator (STRICT: 91 9876543210)
    if (!phone.matches(Regex("^91[6-9][0-9]{9}$"))) {
      Toast.makeText(requireContext(),
        "Enter valid number in format: 91 + 10-digit mobile number",
        Toast.LENGTH_SHORT
      ).show()
      return
    }


    val user = auth.currentUser ?: return

    val orderData = hashMapOf(
      "userId" to user.uid,
      "customerName" to name,
      "customerPhone" to phone,
      "transactionId" to "NO_PAYMENT",
      "productName" to args.name,
      "color" to args.color,
      "quantity" to args.quantity,
      "totalPrice" to totalPrice,
      "status" to "Order Placed",
      "timestamp" to System.currentTimeMillis()
    )

    db.collection("Orders").add(orderData)
      .addOnSuccessListener {
        Toast.makeText(requireContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show()

        notifyAdmins(orderData)

        findNavController().navigate(R.id.action_orderSummaryFragment_to_orderSuccessFragment)
      }
      .addOnFailureListener {
        Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show()
      }
  }

  /** Send FCM notification */
  private fun notifyAdmins(orderData: Map<String, Any>) {
    db.collection("AdminToken").get()
      .addOnSuccessListener { snapshot ->
        snapshot.forEach { doc ->
          val token = doc.getString("token")
          if (!token.isNullOrEmpty()) {
            sendFcmNotification(
              token,
              "📢 New Order Received",
              "Product: ${orderData["productName"]}, Qty: ${orderData["quantity"]}"
            )
          }
        }
      }
  }

  /** Send notification using FCM */
  private fun sendFcmNotification(token: String, title: String, body: String) {
    val client = OkHttpClient()

    val json = JSONObject().apply {
      put("to", token)
      put("notification", JSONObject().apply {
        put("title", title)
        put("body", body)
      })
    }

    val requestBody = RequestBody.create(
      "application/json; charset=utf-8".toMediaTypeOrNull(),
      json.toString()
    )

    val request = Request.Builder()
      .url(FCM_API_URL)
      .post(requestBody)
      .addHeader("Authorization", "key=$FCM_SERVER_KEY")
      .addHeader("Content-Type", "application/json")
      .build()

    client.newCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) = e.printStackTrace()
      override fun onResponse(call: Call, response: Response) {
        println("FCM Response: ${response.body?.string()}")
      }
    })
  }
}
