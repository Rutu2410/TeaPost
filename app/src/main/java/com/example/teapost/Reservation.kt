package com.example.teapost

data class Reservation(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val people: Int,
    val specialEvent: String // New field
)
