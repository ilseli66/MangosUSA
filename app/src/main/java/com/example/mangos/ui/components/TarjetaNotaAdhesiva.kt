package com.example.mangos.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangos.data.CompraMangoEntity

@Composable
fun TarjetaNotaAdhesiva(compra: CompraMangoEntity) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .heightIn(min = 150.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
        border = BorderStroke(1.dp, Color(0xFFFFF59D)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = compra.proveedor,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF212121),
                modifier = Modifier.align(Alignment.TopStart)
            )

            Text(
                text = "${compra.toneladas}\nTon",
                fontSize = 22.sp,
                lineHeight = 22.sp,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFE65100),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}