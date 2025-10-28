package com.example.financefocus.routes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.compose.rememberNavController
import com.example.financefocus.BottomNavItem
import com.example.financefocus.Routes
import com.example.financefocus.apicontract.ApiContract
import com.example.financefocus.dto.Account
import com.example.financefocus.dto.SharedViewModel
import com.example.financefocus.dto.Spent
import com.example.financefocus.dto.UIDebt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun GastosScreen(navController: NavController?, sharedViewModel: SharedViewModel?) {

    var pickableColors = listOf(
        Color(0xFF88909A), // Azul muito claro
        Color(0xFFB99CBB), // Lilás suave
        Color(0xFFAAC5AA), // Verde menta claro
        Color(0xFFAF9D7F), // Pêssego suave
        Color(0xFFB07D8B), // Rosa pálido
        Color(0xFF82ABA9), // Azul água
        Color(0xFFB2B68E), // Amarelo muito pálido
        Color(0xFFB0988C), // Bege claro
        Color(0xFF9481B0), // Lavanda
        Color(0xFF8287A8), // Azul lavanda
        Color(0xFF61959B), // Ciano claro
        Color(0xFF76A279), // Verde claro
        Color(0xFFA47C82), // Rosa claro
        Color(0xFF8D6975), // Rosa bebê
        Color(0xFF9F6EA6), // Lilás claro
        Color(0xFF7B6C91), // Lavanda suave
        Color(0xFF7D87C4), // Azul pálido
        Color(0xFF5C97C2), // Azul céu
        Color(0xFF58B2DA), // Azul gelo
        Color(0xFF33E5DB), // Verde água
        Color(0xFF2CA936), // Verde menta
        Color(0xFFB3FA62), // Verde lima pálido
        Color(0xFFE1EF64), // Amarelo creme
        Color(0xFFA8A584), // Amarelo pálido
        Color(0xFFFA864C)
    )
    var uiDebts = emptyArray<UIDebt>().toMutableList()
    var isListEmpty by remember { mutableStateOf(false) }
    var spentsList by remember { mutableStateOf(listOf<Spent>()) }


    var isSuccessfulDebtsQuery by remember { mutableStateOf(false) }
    var isSuccessfulvAccountQuery by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }

    var client = ApiContract.buildRetroFit()

    if (sharedViewModel != null) {
        Log.println(Log.ASSERT, "Gastos | recuperação user",sharedViewModel.username)
        Log.println(Log.ASSERT, "Gastos | recuperação senha",sharedViewModel.password)
        sharedViewModel.generateToken()
        client.querySpents(sharedViewModel.token, sharedViewModel.username).enqueue(object : Callback<List<Spent>> {
            override fun onResponse(call: Call<List<Spent>>, response: Response<List<Spent>>) {
                if(response.isSuccessful){
                    var gastos = response.body() ?: emptyList()
                    Log.println(Log.ASSERT,"Gastos | gastos cliente",gastos.toString())
                    if(gastos.isEmpty()){
                        isListEmpty = true
                        isSuccessfulDebtsQuery = true
                    }else{
                        spentsList = gastos
                        isListEmpty = false
                        isSuccessfulDebtsQuery = true
                    }
                }else{
                    isSuccessfulDebtsQuery = false
                }
            }

            override fun onFailure(call: Call<List<Spent>>, t: Throwable) {
                isSuccessfulDebtsQuery = false
                sharedViewModel.generateToken()
            }
        })

        client.queryCustomerData(sharedViewModel.token,sharedViewModel.username).enqueue(object: Callback<Account>{
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                Log.println(Log.ASSERT,"account",response.body().toString())
                var conta = response.body()
                if (conta != null) {
                    name = conta.name
                    number = conta.phoneNumber
                    email = conta.email
                    salary = conta.salary.toString()
                    isSuccessfulvAccountQuery = true
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                Log.println(Log.ASSERT,"account",t.toString())
            }
        })
        if(isSuccessfulvAccountQuery&&isSuccessfulDebtsQuery){
            //processamento da ui e porcentagens
            spentsList.forEach{ spent ->
                // 100% --- salary
                // x    --- spent.price
                var percentage = (100 * spent.price)/salary.toDouble()
                uiDebts.add(
                    UIDebt(
                    spent,
                    percentage,
                    pickableColors
                        .shuffled()
                        .stream()
                        .findAny()
                        .get()
                    ))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0052CC))
    ) {
        // Header
        Text(
            text = "GASTOS",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            // Progress Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(bottom = 20.dp)
            ) {
                uiDebts.forEach { uiElement ->
                    Box(
                        modifier = Modifier
                            .weight((uiElement.percentage/100f).toFloat())
                            .fillMaxHeight()
                            .background(uiElement.color)
                    )
                }
            }

            // Progress percentages
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ) {
                uiDebts.forEach { uiElement ->
                    Text(
                        text = "%.2f ".format(uiElement.percentage) + "%",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.weight((uiElement.percentage/100f).toFloat())
                    )
                }
            }
            // processador de lista de gastos
            if(isSuccessfulDebtsQuery&&spentsList.isNotEmpty()){
                uiDebts.forEach{ uiElement: UIDebt ->
                    // Category Items
                    CategoryItem(
                        color = uiElement.color,
                        label = uiElement.debt.name,
                        value = uiElement.debt.price.toString()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }else if (isListEmpty){
                Text(
                    text = "Nenhum gasto registrado.",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }else {
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }



            Spacer(modifier = Modifier.weight(1f))

            // Add Button
            FloatingActionButton(
                onClick = { /* Add functionality */ },
                containerColor = Color(0xFF6A1B9A),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = Color.White
                )
            }

            Text(
                text = "Adicionar",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }

        // Bottom Navigation
        BottomNavigation(navController = navController, currentScreen = "Gastos")
    }
}

@Composable
fun CategoryItem(
    color: Color,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "R$",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Substitui o OutlinedTextField por Text
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.width(120.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavigation(
    navController: NavController?,
    currentScreen: String = "Gastos"
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

// E atualize o BottomNavItem para aceitar onClick:
@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }  // ← Adicione esta linha
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GastosScreenPreview() {
    GastosScreen(navController = rememberNavController(),sharedViewModel = null)
}