package com.example.stockcontrol.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, NOMBRE_BD, null, VERSION_BD) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLA_PRODUCTOS (" +
                    "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_NOMBRE TEXT NOT NULL, " +
                    "$COL_PRECIO REAL NOT NULL, " +
                    "$COL_CANTIDAD INTEGER NOT NULL)"
        )
        db.execSQL(
            "CREATE TABLE $TABLA_VENTAS (" +
                    "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_PRODUCTO_ID INTEGER NOT NULL, " +
                    "$COL_CANTIDAD INTEGER NOT NULL, " +
                    "$COL_TOTAL REAL NOT NULL, " +
                    "$COL_FECHA TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_VENTAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PRODUCTOS")
        onCreate(db)
    }

    companion object {
        const val NOMBRE_BD = "stockcontrol.db"
        const val VERSION_BD = 1

        const val TABLA_PRODUCTOS = "productos"
        const val TABLA_VENTAS = "ventas"

        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_PRECIO = "precio"
        const val COL_CANTIDAD = "cantidad"
        const val COL_PRODUCTO_ID = "producto_id"
        const val COL_TOTAL = "total"
        const val COL_FECHA = "fecha"
    }
}