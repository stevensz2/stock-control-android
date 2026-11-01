package com.example.stockcontrol.util

import com.example.stockcontrol.model.Producto

object Reglas {
    const val STOCK_MINIMO = 5

    fun validarProducto(nombre: String, precio: Double?, cantidad: Int?): String? {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (precio == null || precio <= 0.0) return "El precio debe ser mayor que 0"
        if (cantidad == null || cantidad < 0) return "La cantidad no puede ser negativa"
        return null
    }

    fun puedeVender(stockActual: Int, cantidad: Int): Boolean {
        return cantidad > 0 && cantidad <= stockActual
    }

    fun totalVenta(precioUnitario: Double, cantidad: Int): Double {
        return precioUnitario * cantidad
    }

    fun esStockBajo(stock: Int): Boolean {
        return stock <= STOCK_MINIMO
    }

    fun valorInventario(productos: List<Producto>): Double {
        return productos.sumOf { it.precio * it.cantidad }
    }
}