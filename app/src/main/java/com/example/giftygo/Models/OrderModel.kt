package com.example.giftygo.Models

data class OrderModel(
    var orderId: String? = "",
    var documentId: String = "",
    var userId: String = "",
    var productName: String = "",
    var color: String = "",
    var quantity: Int = 0,
    var pricePerItem: Int = 0,
    var totalPrice: Int = 0,
    var customerName: String = "",
    var customerPhone: String = "",
    var transactionId: String = "",
    var paymentStatus: String = "",
    var orderStatus: String = "Order placed",
    var orderTime: Long = 0,
    var status: String = ""
)
