package com.example.financefocus.routes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.financefocus.dto.SpentRequest
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
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        )

        // Progress Bars - Gráfico Visual com Barras Separadas
        if (uiDebts.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp)
            ) {
                // Títulos
                Text(
                    text = "Distribuição de Gastos",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Gráfico com barras
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    uiDebts.forEach { uiElement ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Barra
                            Box(
                                modifier = Modifier
                                    .width(32.dp)
                                    .fillMaxHeight(fraction = (uiElement.percentage / 100f).coerceIn(
                                        0.1, 1.0
                                    )
                                        .toFloat())
                                    .background(uiElement.color)
                            )

                            // Porcentagem
                            Text(
                                text = String.format("%.1f%%", uiElement.percentage),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            // Nome do gasto
                            Text(
                                text = uiElement.debt.name,
                                color = Color.White,
                                fontSize = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            // processador de lista de gastos
            if(isSuccessfulDebtsQuery&&spentsList.isNotEmpty()){
                uiDebts.forEach{ uiElement: UIDebt ->
                    // Category Items
                    CategoryItem(
                        color = uiElement.color,
                        label = uiElement.debt.name,
                        value = uiElement.debt.price.toString(),
                        spentId = uiElement.debt.id,
                        sharedViewModel = sharedViewModel,
                        navController = navController,
                        spent = uiElement.debt,
                        onDelete = {
                            // Remova o item da lista após a exclusão bem-sucedida
                            spentsList = spentsList.filter { it.id != uiElement.debt.id }
                            uiDebts = uiDebts.filter { it.debt.id != uiElement.debt.id }.toMutableList()
                        }
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
                onClick = {
                    if (sharedViewModel != null) {
                        val spentRequest = SpentRequest(
                            clientUsername = sharedViewModel.username,
                            price = 0.0,  // This will be filled in the next screen
                            name = ""     // This will be filled in the next screen
                        )

                        // Store the spent request in shared view model for the next screen
                        sharedViewModel.currentSpentRequest = spentRequest

                        // Navigate to add spent screen
                        navController?.navigate(Routes.ADDSPENT)
                    }
                },
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
    value: String,
    spentId: Long,
    sharedViewModel: SharedViewModel?,
    navController: NavController? = null,
    spent: Spent? = null,
    onDelete: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(false) }

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

        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.width(100.dp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(12.dp))

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (sharedViewModel != null && spent != null) {
                            sharedViewModel.currentSpentToUpdate = spent
                            navController?.navigate(Routes.EDITSPENT)
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (sharedViewModel != null) {
                            isLoading = true
                            val client = ApiContract.buildRetroFit()
                            client.deleteSpent(sharedViewModel.token, spentId).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    isLoading = false
                                    if (response.isSuccessful) {
                                        onDelete()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    isLoading = false
                                    Log.e("DeleteSpent", "Error deleting spent", t)
                                }
                            })
                        }
                    }
            )
        }

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