package com.example.teapost

data class Item(
    val id: Int,
    val name: String,
    val price: Int?,
    val img: ByteArray? = null
)
