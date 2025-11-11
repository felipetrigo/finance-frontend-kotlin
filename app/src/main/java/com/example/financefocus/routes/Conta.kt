package com.example.financefocus

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financefocus.apicontract.ApiContract
import com.example.financefocus.dto.Account
import com.example.financefocus.dto.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Conta(navController: NavController?, sharedViewModel: SharedViewModel?) {
    var client = ApiContract.buildRetroFit()
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var loaded by remember { mutableStateOf(false) }
    if (sharedViewModel != null) {
        Log.println(Log.ASSERT,"minha tag",sharedViewModel.username)
        Log.println(Log.ASSERT,"minha tag",sharedViewModel.password)
        client.queryCustomerData(sharedViewModel.token,sharedViewModel.username).enqueue(object: Callback<Account>{
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                Log.println(Log.ASSERT,"account",response.body().toString())
                var conta = response.body()
                if (conta != null) {
                    name = conta.name
                    number = conta.phoneNumber
                    email = conta.email
                    salary = conta.salary.toString()
                    loaded = true
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                Log.println(Log.ASSERT,"account",t.toString())
            }
        })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0052CC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header

            // Botão Sair no topo direito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Navegar de volta para a tela inicial
                        navController?.navigate(Routes.START) {
                            popUpTo(Routes.START) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE60049),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Sair",
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "CONTA",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
            if (loaded) {
            // Avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar do usuário",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Informações do usuário
                UserInfoCard(
                    icon = Icons.Default.Person,
                    text = name,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoCard(
                    icon = Icons.Default.Phone,
                    text = number,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoCard(
                    icon = Icons.Default.Email,
                    text = email,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoCard(
                    icon = Icons.Default.AttachMoney,
                    text = salary,
                    backgroundColor = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            }
        if(!loaded) {
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        // Bottom Navigation
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                navController = navController,
                currentScreen = "conta"
            )
        }
    }
}

@Composable
fun UserInfoCard(
    icon: ImageVector? = null,
    text: String,
    backgroundColor: Color = Color.White
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF0052CC),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = text,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = if (icon == null) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController?,
    currentScreen: String = "conta"
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Conta",
                isSelected = currentScreen == "conta",
                onClick = {
                    if (currentScreen != "conta") {
                        navController?.navigate(Routes.CONTA)
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Início",
                isSelected = currentScreen == "inicio",
                onClick = {
                    navController?.navigate(Routes.GASTOS)
                }
            )

            BottomNavItem(
                icon = Icons.Default.School,
                label = "Aprender",
                isSelected = currentScreen == "aprender",
                onClick = {
                    navController?.navigate(Routes.LEARNING)
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
            .padding(8.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF0052CC) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = if (isSelected) Color(0xFF0052CC) else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContaScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0052CC)
    ) {
        Conta(navController = null, sharedViewModel = null)
    }
}