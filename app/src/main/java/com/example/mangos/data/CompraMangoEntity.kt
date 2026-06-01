package com.example.mangos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_compras_mangos")
data class CompraMangoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val proveedor: String,
    val toneladas: Double,
    val fechaTimestamp: Long = System.currentTimeMillis()
)