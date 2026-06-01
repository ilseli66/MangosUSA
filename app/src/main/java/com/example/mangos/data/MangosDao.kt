package com.example.mangos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraMangoDao {

    @Query("SELECT * FROM tabla_compras_mangos ORDER BY fechaTimestamp DESC")
    fun obtenerTodasLasCompras(): Flow<List<CompraMangoEntity>>

    @Query("SELECT SUM(toneladas) FROM tabla_compras_mangos")
    fun obtenerToneladasTotales(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCompra(compra: CompraMangoEntity)

    @Query("DELETE FROM tabla_compras_mangos")
    suspend fun borrarTodasLasCompras()
}