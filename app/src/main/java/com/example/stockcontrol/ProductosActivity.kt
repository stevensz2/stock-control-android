package com.example.stockcontrol

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stockcontrol.data.ProductoRepository
import com.example.stockcontrol.model.Producto
import com.example.stockcontrol.util.Reglas

class ProductosActivity : AppCompatActivity() {

    private lateinit var repo: ProductoRepository
    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etCantidad: EditText
    private lateinit var lvProductos: ListView

    private var productos: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        repo = ProductoRepository(this)
        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etCantidad = findViewById(R.id.etCantidad)
        lvProductos = findViewById(R.id.lvProductos)
        val btnAgregar: Button = findViewById(R.id.btnAgregar)

        btnAgregar.setOnClickListener { agregarProducto() }

        lvProductos.setOnItemLongClickListener { _, _, posicion, _ ->
            confirmarBorrado(productos[posicion])
            true
        }

        cargarLista()
    }

    private fun agregarProducto() {
        val nombre = etNombre.text.toString().trim()
        val precio = etPrecio.text.toString().toDoubleOrNull()
        val cantidad = etCantidad.text.toString().toIntOrNull()

        val error = Reglas.validarProducto(nombre, precio, cantidad)
        if (error != null || precio == null || cantidad == null) {
            Toast.makeText(this, error ?: "Datos inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        repo.insertar(Producto(nombre = nombre, precio = precio, cantidad = cantidad))
        etNombre.text.clear()
        etPrecio.text.clear()
        etCantidad.text.clear()
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
        cargarLista()
    }

    private fun cargarLista() {
        productos = repo.listar()
        val textos = productos.map {
            val aviso = if (Reglas.esStockBajo(it.cantidad)) "  ⚠ Stock bajo" else ""
            "${it.nombre}\nPrecio: ${it.precio}  |  Stock: ${it.cantidad}$aviso"
        }
        lvProductos.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
    }

    private fun confirmarBorrado(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Borrar producto")
            .setMessage("¿Borrar ${producto.nombre}?")
            .setPositiveButton("Borrar") { _, _ ->
                repo.borrar(producto.id)
                cargarLista()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}