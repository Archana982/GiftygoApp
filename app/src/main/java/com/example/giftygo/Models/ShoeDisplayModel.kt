package com.example.giftygo.Models

data class ShoeDisplayModel(
    var id: String? = null,
    var name: String? = null,
    var description: String? = null,
    var color: String? = null,
    val price: Int = 0,
    var imgUrl: String? = null,
    var glbUrl: String? = null
)