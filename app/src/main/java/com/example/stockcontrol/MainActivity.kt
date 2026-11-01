package com.example.stockcontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnProductos: Button = findViewById(R.id.btnProductos)
        btnProductos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }

        val btnInventario: Button = findViewById(R.id.btnInventario)
        btnInventario.setOnClickListener {
            startActivity(Intent(this, InventarioActivity::class.java))
        }

        val btnVentas: Button = findViewById(R.id.btnVentas)
        btnVentas.setOnClickListener {
            startActivity(Intent(this, VentasActivity::class.java))
        }

        val btnReportes: Button = findViewById(R.id.btnReportes)
        btnReportes.setOnClickListener {
            startActivity(Intent(this, ReportesActivity::class.java))
        }
    }
}