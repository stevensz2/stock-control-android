package com.example.stockcontrol

import com.example.stockcontrol.util.Reglas

import com.example.stockcontrol.model.Producto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasTest {

    // validarProducto
    @Test
    fun validarProducto_datosCorrectos_devuelveNull() {
        assertNull(Reglas.validarProducto("Arroz", 2500.0, 10))
    }

    @Test
    fun validarProducto_nombreVacio_devuelveError() {
        assertNotNull(Reglas.validarProducto("   ", 2500.0, 10))
    }

    @Test
    fun validarProducto_precioCero_devuelveError() {
        assertNotNull(Reglas.validarProducto("Arroz", 0.0, 10))
    }

    @Test
    fun validarProducto_precioNulo_devuelveError() {
        assertNotNull(Reglas.validarProducto("Arroz", null, 10))
    }

    @Test
    fun validarProducto_cantidadNegativa_devuelveError() {
        assertNotNull(Reglas.validarProducto("Arroz", 2500.0, -1))
    }

    @Test
    fun validarProducto_cantidadNula_devuelveError() {
        assertNotNull(Reglas.validarProducto("Arroz", 2500.0, null))
    }

    // puedeVender
    @Test
    fun puedeVender_cantidadMenorAlStock_esVerdadero() {
        assertTrue(Reglas.puedeVender(10, 3))
    }

    @Test
    fun puedeVender_cantidadIgualAlStock_esVerdadero() {
        assertTrue(Reglas.puedeVender(5, 5))
    }

    @Test
    fun puedeVender_cantidadMayorAlStock_esFalso() {
        assertFalse(Reglas.puedeVender(1, 5))
    }

    @Test
    fun puedeVender_cantidadCeroONegativa_esFalso() {
        assertFalse(Reglas.puedeVender(10, 0))
        assertFalse(Reglas.puedeVender(10, -2))
    }

    // totalVenta
    @Test
    fun totalVenta_multiplicaPrecioPorCantidad() {
        assertEquals(5000.0, Reglas.totalVenta(2500.0, 2), 0.001)
    }

    // esStockBajo
    @Test
    fun esStockBajo_enElMinimo_esVerdadero() {
        assertTrue(Reglas.esStockBajo(Reglas.STOCK_MINIMO))
    }

    @Test
    fun esStockBajo_porEncimaDelMinimo_esFalso() {
        assertFalse(Reglas.esStockBajo(Reglas.STOCK_MINIMO + 1))
    }

    // valorInventario
    @Test
    fun valorInventario_sumaPrecioPorCantidad() {
        val productos = listOf(
            Producto(1, "A", 1000.0, 2),
            Producto(2, "B", 500.0, 4)
        )
        assertEquals(4000.0, Reglas.valorInventario(productos), 0.001)
    }

    @Test
    fun valorInventario_listaVacia_esCero() {
        assertEquals(0.0, Reglas.valorInventario(emptyList()), 0.001)
    }
}