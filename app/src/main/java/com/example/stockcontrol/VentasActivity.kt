package com.example.stockcontrol

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stockcontrol.data.ProductoRepository
import com.example.stockcontrol.data.VentaRepository
import com.example.stockcontrol.model.Producto
import com.example.stockcontrol.util.Reglas

class VentasActivity : AppCompatActivity() {

    private lateinit var productoRepo: ProductoRepository
    private lateinit var ventaRepo: VentaRepository
    private lateinit var spProductos: Spinner
    private lateinit var etCantidadVenta: EditText
    private lateinit var tvTotalVenta: TextView

    private var productos: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        productoRepo = ProductoRepository(this)
        ventaRepo = VentaRepository(this)
        spProductos = findViewById(R.id.spProductos)
        etCantidadVenta = findViewById(R.id.etCantidadVenta)
        tvTotalVenta = findViewById(R.id.tvTotalVenta)
        val btnRegistrarVenta: Button = findViewById(R.id.btnRegistrarVenta)

        spProductos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                actualizarTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                actualizarTotal()
            }
        }

        etCantidadVenta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                actualizarTotal()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegistrarVenta.setOnClickListener { registrarVenta() }

        cargarProductos()
    }

    private fun cargarProductos() {
        productos = productoRepo.listar()
        val textos = productos.map { "${it.nombre} (stock: ${it.cantidad})" }
        spProductos.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, textos)
        actualizarTotal()
    }

    private fun actualizarTotal() {
        val posicion = spProductos.selectedItemPosition
        val cantidad = etCantidadVenta.text.toString().toIntOrNull() ?: 0
        if (productos.isEmpty() || posicion < 0 || posicion >= productos.size) {
            tvTotalVenta.text = "Total: $0"
            return
        }
        val total = Reglas.totalVenta(productos[posicion].precio, cantidad)
        tvTotalVenta.text = "Total: $$total"
    }

    private fun registrarVenta() {
        val posicion = spProductos.selectedItemPosition
        if (productos.isEmpty() || posicion < 0 || posicion >= productos.size) {
            Toast.makeText(this, "Primero agrega productos", Toast.LENGTH_SHORT).show()
            return
        }
        val cantidad = etCantidadVenta.text.toString().toIntOrNull()
        if (cantidad == null) {
            Toast.makeText(this, "Escribe la cantidad a vender", Toast.LENGTH_SHORT).show()
            return
        }

        val error = ventaRepo.registrarVenta(productos[posicion].id, cantidad)
        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show()
            etCantidadVenta.text.clear()
            cargarProductos()
        }
    }
}