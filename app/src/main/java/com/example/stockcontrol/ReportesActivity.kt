package com.example.stockcontrol

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.stockcontrol.data.ProductoRepository
import com.example.stockcontrol.data.VentaRepository
import com.example.stockcontrol.util.Reglas

class ReportesActivity : AppCompatActivity() {

    private lateinit var productoRepo: ProductoRepository
    private lateinit var ventaRepo: VentaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        productoRepo = ProductoRepository(this)
        ventaRepo = VentaRepository(this)
    }

    override fun onResume() {
        super.onResume()
        mostrarReporte()
    }

    private fun mostrarReporte() {
        val productos = productoRepo.listar()
        val stockBajo = productos.count { Reglas.esStockBajo(it.cantidad) }

        findViewById<TextView>(R.id.tvValorInventario).text =
            "Valor del inventario: $${Reglas.valorInventario(productos)}"
        findViewById<TextView>(R.id.tvTotalProductos).text =
            "Productos registrados: ${productos.size}"
        findViewById<TextView>(R.id.tvStockBajo).text =
            "Productos con stock bajo: $stockBajo"
        findViewById<TextView>(R.id.tvNumeroVentas).text =
            "Ventas realizadas: ${ventaRepo.numeroVentas()}"
        findViewById<TextView>(R.id.tvTotalVendido).text =
            "Total vendido: $${ventaRepo.totalVendido()}"
    }
}