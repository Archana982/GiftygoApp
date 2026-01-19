package com.example.giftygo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.giftygo.databinding.DialogMobileLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class DialogMobileLoginActivity : AppCompatActivity() {

  private lateinit var binding: DialogMobileLoginBinding
  private val db = FirebaseFirestore.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DialogMobileLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnProceed.setOnClickListener {
      val phone = binding.etAdminMobile.text.toString().trim()

      if (phone.isEmpty()) {
        Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      // 🔍 Check Firestore for existing admin
      db.collection("users").document(phone)
        .get()
        .addOnSuccessListener { doc ->
          if (doc.exists()) {
            val role = doc.getString("role")
            val name = doc.getString("name")

            if (role == "admin") {
              Toast.makeText(this, "Welcome Admin $name!", Toast.LENGTH_SHORT).show()
              startActivity(Intent(this, AdminPanelActivity::class.java))
              finish()
            } else {
              Toast.makeText(this, "Access denied: Not an admin", Toast.LENGTH_SHORT).show()
            }
          } else {
            Toast.makeText(this, "No admin found with this number", Toast.LENGTH_SHORT).show()
          }
        }
        .addOnFailureListener {
          Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
  }
}
