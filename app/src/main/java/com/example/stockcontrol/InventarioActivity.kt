package com.example.stockcontrol

import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.stockcontrol.data.ProductoRepository
import com.example.stockcontrol.model.Producto
import com.example.stockcontrol.util.Reglas

class InventarioActivity : AppCompatActivity() {

    private lateinit var repo: ProductoRepository
    private lateinit var lvInventario: ListView
    private var productos: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        repo = ProductoRepository(this)
        lvInventario = findViewById(R.id.lvInventario)

        lvInventario.setOnItemClickListener { _, _, posicion, _ ->
            mostrarDialogoAjuste(productos[posicion])
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLista()
    }

    private fun cargarLista() {
        productos = repo.listar()
        val textos = productos.map {
            val aviso = if (Reglas.esStockBajo(it.cantidad)) "  ⚠ Stock bajo" else ""
            "${it.nombre}\nStock: ${it.cantidad}$aviso"
        }
        lvInventario.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
    }

    private fun mostrarDialogoAjuste(producto: Producto) {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.hint = "Nueva cantidad"
        input.setText(producto.cantidad.toString())

        AlertDialog.Builder(this)
            .setTitle("Ajustar stock: ${producto.nombre}")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nueva = input.text.toString().toIntOrNull()
                if (nueva == null || nueva < 0) {
                    Toast.makeText(this, "La cantidad no puede ser negativa", Toast.LENGTH_SHORT).show()
                } else {
                    repo.actualizarCantidad(producto.id, nueva)
                    Toast.makeText(this, "Stock actualizado", Toast.LENGTH_SHORT).show()
                    cargarLista()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}