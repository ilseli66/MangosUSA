package com.example.mangos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mangos.data.AppDatabase
import com.example.mangos.data.repository.CompraMangoRepository
import com.example.mangos.notification.NotificationHelper
import com.example.mangos.ui.InterfaceUI
import com.example.mangos.ui.theme.MangosTheme
import com.example.mangos.ui.viewmodel.CompraMangoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.crearCanalNotificaciones()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CompraMangoRepository(database.compraMangoDao())
        val viewModel = CompraMangoViewModel(repository)

        setContent {
            val context = LocalContext.current

            // Sistema adaptativo de control de peticiones de Alertas en Android 13+
            val launcherPermiso = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { _ -> }

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        launcherPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            MangosTheme {
                InterfaceUI(
                    viewModel = viewModel,
                    notificationHelper = notificationHelper
                )
            }
        }
    }
}