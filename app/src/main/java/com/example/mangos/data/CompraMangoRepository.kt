package com.example.mangos.data.repository

import com.example.mangos.data.CompraMangoDao
import com.example.mangos.data.CompraMangoEntity
import kotlinx.coroutines.flow.Flow

class CompraMangoRepository(private val compraMangoDao: CompraMangoDao) {

    val todasLasCompras: Flow<List<CompraMangoEntity>> = compraMangoDao.obtenerTodasLasCompras()

    val toneladasTotales: Flow<Double?> = compraMangoDao.obtenerToneladasTotales()

    suspend fun registrarCompra(compra: CompraMangoEntity) {
        compraMangoDao.insertarCompra(compra)
    }

    suspend fun borrarTodasLasCompras() {
        compraMangoDao.borrarTodasLasCompras()
    }
}