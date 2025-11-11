package com.example.financefocus.routes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financefocus.Routes
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController,
    videoUri: String,
    videoTitle: String,
    videoAuthor: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
    Button(
        onClick = {
            // Navegar de volta para a tela inicial
            navController?.navigate(Routes.LEARNING) {
                popUpTo(Routes.LEARNING) { inclusive = true }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00E676),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.height(40.dp)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "voltar",
            modifier = Modifier.size(16.dp)
        )
    }}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // YouTube Icon Button
        IconButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, videoUri.toUri())
                context.startActivity(intent)
            },
            modifier = Modifier
                .size(300.dp)
                .padding(top = 40.dp, bottom = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Open in YouTube",
                tint = Color.Black,
                modifier = Modifier.size(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Video Information
        Text(
            text = videoTitle,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = videoAuthor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
