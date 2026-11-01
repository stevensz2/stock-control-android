package com.example.stockcontrol.data

import android.content.ContentValues
import android.content.Context
import com.example.stockcontrol.util.Reglas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VentaRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Registra una venta y descuenta el stock.
     * Devuelve null si todo salió bien, o un mensaje de error.
     */
    fun registrarVenta(productoId: Long, cantidad: Int): String? {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val cursor = db.query(
                DatabaseHelper.TABLA_PRODUCTOS,
                arrayOf(DatabaseHelper.COL_PRECIO, DatabaseHelper.COL_CANTIDAD),
                "${DatabaseHelper.COL_ID} = ?",
                arrayOf(productoId.toString()),
                null, null, null
            )
            var precio = 0.0
            var stock = 0
            var existe = false
            cursor.use {
                if (it.moveToFirst()) {
                    existe = true
                    precio = it.getDouble(0)
                    stock = it.getInt(1)
                }
            }

            if (!existe) return "El producto no existe"
            if (!Reglas.puedeVender(stock, cantidad)) {
                return "Cantidad inválida o stock insuficiente (disponible: $stock)"
            }

            val total = Reglas.totalVenta(precio, cantidad)
            val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(Date())

            val venta = ContentValues().apply {
                put(DatabaseHelper.COL_PRODUCTO_ID, productoId)
                put(DatabaseHelper.COL_CANTIDAD, cantidad)
                put(DatabaseHelper.COL_TOTAL, total)
                put(DatabaseHelper.COL_FECHA, fecha)
            }
            db.insert(DatabaseHelper.TABLA_VENTAS, null, venta)

            val nuevoStock = ContentValues().apply {
                put(DatabaseHelper.COL_CANTIDAD, stock - cantidad)
            }
            db.update(
                DatabaseHelper.TABLA_PRODUCTOS,
                nuevoStock,
                "${DatabaseHelper.COL_ID} = ?",
                arrayOf(productoId.toString())
            )

            db.setTransactionSuccessful()
            return null
        } finally {
            db.endTransaction()
        }
    }

    fun totalVendido(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COALESCE(SUM(${DatabaseHelper.COL_TOTAL}), 0) " +
                    "FROM ${DatabaseHelper.TABLA_VENTAS}",
            null
        )
        cursor.use {
            return if (it.moveToFirst()) it.getDouble(0) else 0.0
        }
    }

    fun numeroVentas(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM ${DatabaseHelper.TABLA_VENTAS}",
            null
        )
        cursor.use {
            return if (it.moveToFirst()) it.getInt(0) else 0
        }
    }
}