package com.example.mangos.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mangos.notification.NotificationHelper
import com.example.mangos.ui.components.TarjetaNotaAdhesiva
import com.example.mangos.ui.viewmodel.CompraMangoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon

@Composable
fun InterfaceUI(
    viewModel: CompraMangoViewModel,
    notificationHelper: NotificationHelper
){
    val context = LocalContext.current
    val navController = rememberNavController()

    // RECOLECCIÓN DIRECTA DE ESTADOS (Reactividad MVVM)
    val listaCompras by viewModel.listaComprasState.collectAsState()
    val toneladasActuales by viewModel.toneladasActualesState.collectAsState()
    val cantMax by viewModel.cantMaxState.collectAsState()

    val limiteMaximoDiario = cantMax.toDoubleOrNull() ?: 0.0

    NavHost(navController = navController, startDestination = "inicio") {

        // --- PANTALLA: INICIO (PIZARRA DIGITAL) ---
        composable("inicio") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(size = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                            .padding(all = 12.dp)
                    ) {
                        Text(
                            text = " Pizarra 'Mangos USA' ",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Progreso Total: $toneladasActuales / $limiteMaximoDiario Ton",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (listaCompras.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(160.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay compras registradas", color = Color.Gray)
                            }
                        } else {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth().height(160.dp)
                            ) {
                                items(listaCompras) { compra ->
                                    TarjetaNotaAdhesiva(compra = compra)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier.width(220.dp).height(62.dp),
                        value = cantMax,
                        onValueChange = { viewModel.cantMaxState.value = it }, // Mutación directa en el stream del VM
                        label = { Text("Cantidad Máxima Diaria (Ton.)", fontSize = 11.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color(0xFF757575)
                        )
                    )

                    Button(
                        onClick = {
                            viewModel.borrarHistorial()
                            Toast.makeText(context, "Historial de compras eliminado", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F), // Color rojo de advertencia
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f) // Absorbe de forma estética el espacio del centro
                            .padding(horizontal = 8.dp),
                        contentPadding = PaddingValues(0.dp) // Quita paddings internos para que el ícono quede bien centrado
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar historial",
                            modifier = Modifier.size(24.dp) // Tamaño estándar ideal para íconos
                        )
                    }

                    Button(
                        onClick = { navController.navigate("registro de compra") },
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB300),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "+", fontSize = 28.sp, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        // --- PANTALLA: FORMULARIO DE REGISTRO ---
        composable("registro de compra") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(40.dp, 150.dp, 40.dp, 230.dp),
                shape = RoundedCornerShape(size = 20.dp),
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color(0xFFFFB300))
                        .fillMaxSize()
                ) {
                    var provedor by remember { mutableStateOf("") }
                    var cantidad by remember { mutableStateOf("") }

                    OutlinedTextField(
                        modifier = Modifier.padding(15.dp).fillMaxWidth(),
                        value = provedor,
                        onValueChange = { provedor = it },
                        label = { Text("Proveedor de Mangos") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.padding(15.dp).fillMaxWidth(),
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad Comprada") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = {
                                viewModel.intentarRegistrarCompra(
                                    proveedor = provedor,
                                    cantidadStr = cantidad,
                                    onSuccess = {
                                        notificationHelper.lanzarNotificacionCompra(provedor, cantidad.toDoubleOrNull() ?: 0.0)
                                        navController.navigate("inicio") {
                                            popUpTo("inicio") { inclusive = true }
                                        }
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFFFFB300))
                        ) {
                            Text(text = "Registrar Compra", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}