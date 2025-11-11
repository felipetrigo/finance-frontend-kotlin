package com.example.financefocus.routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financefocus.R
import com.seuapp.financefocus.ui.theme.FinanceFocusTheme
import com.example.financefocus.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0052CC)
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }

            }
        }
    }
}

@Composable
fun StartScreen(
    navController: NavController?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Finance Focus",
            modifier = Modifier
                .height(300.dp)
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                navController?.navigate(Routes.LOGIN)
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Entrar", fontSize = 18.sp)
        }

        Button(
            onClick = {
                navController?.navigate(Routes.CADASTRO)
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Cadastrar Conta", fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    FinanceFocusTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0052CC)
        ) {
            StartScreen(
                navController = null
            )
        }
    }
}
