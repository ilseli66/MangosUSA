package com.example.mangos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangos.data.CompraMangoEntity
import com.example.mangos.data.repository.CompraMangoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CompraMangoViewModel(private val repository: CompraMangoRepository) : ViewModel() {

    val cantMaxState = MutableStateFlow("100.0")

    val listaComprasState: StateFlow<List<CompraMangoEntity>> = repository.todasLasCompras
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val toneladasActualesState: StateFlow<Double> = repository.toneladasTotales
        .map { it ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun intentarRegistrarCompra(
        proveedor: String,
        cantidadStr: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val toneladasAAgregar = cantidadStr.toDoubleOrNull() ?: 0.0
        val limiteMaximoDiario = cantMaxState.value.toDoubleOrNull() ?: 0.0
        val toneladasActuales = toneladasActualesState.value

        if (proveedor.isBlank() || toneladasAAgregar <= 0.0) {
            onError("Campos inválidos")
            return
        }

        if (toneladasActuales + toneladasAAgregar > limiteMaximoDiario) {
            onError("Error: Supera el límite establecido ($limiteMaximoDiario Ton)")
            return
        }

        viewModelScope.launch {
            val nuevaCompra = CompraMangoEntity(
                proveedor = proveedor,
                toneladas = toneladasAAgregar
            )
            repository.registrarCompra(nuevaCompra)
            onSuccess()
        }
    }

    fun borrarHistorial() {
        viewModelScope.launch {
            repository.borrarTodasLasCompras()
        }
    }
}