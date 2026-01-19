package com.example.giftygo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Step 1: Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // ✅ Step 2: Update FCM token (if user is logged in)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                FirebaseFirestore.getInstance().collection("Users")
                    .document(currentUser.uid)
                    .update("fcmToken", token)
            }
        }

        // ✅ Step 3: Preload Razorpay SDK
        Checkout.preload(applicationContext)

        // ✅ Step 4: Set StatusBar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    // ✅ Razorpay payment success callback
    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Success: $razorpayPaymentId", Toast.LENGTH_SHORT).show()
        // You can notify the fragment if needed here
    }

    // ✅ Razorpay payment error callback
    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
    }
}
