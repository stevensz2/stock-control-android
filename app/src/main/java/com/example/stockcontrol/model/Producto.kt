package com.example.stockcontrol.model

data class Producto(
    val id: Long = 0,
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)