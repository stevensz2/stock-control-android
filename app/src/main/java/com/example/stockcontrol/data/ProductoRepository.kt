package com.example.stockcontrol.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.stockcontrol.model.Producto

class ProductoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertar(producto: Producto): Long {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.COL_NOMBRE, producto.nombre)
            put(DatabaseHelper.COL_PRECIO, producto.precio)
            put(DatabaseHelper.COL_CANTIDAD, producto.cantidad)
        }
        return db.insert(DatabaseHelper.TABLA_PRODUCTOS, null, valores)
    }

    fun listar(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            null, null, null, null, null,
            "${DatabaseHelper.COL_NOMBRE} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(aProducto(it))
            }
        }
        return lista
    }

    fun obtenerPorId(id: Long): Producto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLA_PRODUCTOS,
            null,
            "${DatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            return if (it.moveToFirst()) aProducto(it) else null
        }
    }

    fun borrar(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLA_PRODUCTOS,
            "${DatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun actualizarCantidad(id: Long, nuevaCantidad: Int): Int {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.COL_CANTIDAD, nuevaCantidad)
        }
        return db.update(
            DatabaseHelper.TABLA_PRODUCTOS,
            valores,
            "${DatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun aProducto(c: Cursor): Producto {
        return Producto(
            id = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
            nombre = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_NOMBRE)),
            precio = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_PRECIO)),
            cantidad = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COL_CANTIDAD))
        )
    }
}