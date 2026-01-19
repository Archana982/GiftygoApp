package com.example.giftygo.Models

data class CartModel(
    var orderId: String = "",
    val name: String = "",
    val price: Int = 0,
    val imgUrl: String = "",
    val glbUrl: String = "",
    var color: String = "",
    val pid: String = "",
    val uid: String = "",
    var orderStatus: String = "Pending",
    var quantity: Int = 1,  // Quantity default is 1
    var documentId: String? = null // Firestore document ID
)


