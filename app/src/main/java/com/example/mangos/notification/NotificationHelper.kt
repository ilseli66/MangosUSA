package com.example.mangos.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CANAL_ID = "CANAL_MANGOS_USA"
        const val CANAL_NOMBRE = "Notificaciones de Compras"
        const val CANAL_DESCRIPCION = "Avisos cuando se añade una nueva nota a la pizarra"
    }

    // Inicializa el canal obligatorio para Android 8.0 o superior
    fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CANAL_ID, CANAL_NOMBRE, importance).apply {
                description = CANAL_DESCRIPCION
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Lanza la alerta visual en la barra de estado
    fun lanzarNotificacionCompra(proveedor: String, toneladas: Double) {
        val builder = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icono nativo del sistema
            .setContentTitle("Pizarra Mangos USA")
            .setContentText("Se ha añadido una nota nueva: $proveedor compró $toneladas Ton.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}