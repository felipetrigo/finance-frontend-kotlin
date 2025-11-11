package com.example.financefocus.routes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financefocus.Routes
import com.example.financefocus.apicontract.ApiContract
import com.example.financefocus.dto.Account
import com.example.financefocus.dto.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpentScreen(navController: NavController?, sharedViewModel: SharedViewModel?) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var spentName by remember { mutableStateOf("") }
    var spentValue by remember { mutableStateOf("") }
    val spentValueRef = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0052CC))
            .padding(20.dp)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController?.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }

            Text(
                text = "ADICIONAR GASTO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        TextField(
            value = spentName,
            onValueChange = { spentName = it },
            label = { Text("Nome") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move o foco para o próximo campo
                    spentValueRef.requestFocus()
                }
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = spentValue,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    spentValue = it
                }
            },
            label = { Text("Preço") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(spentValueRef)
                .padding(bottom = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(20.dp))
        } else {
            Button(
                onClick = {
                    if (spentName.isBlank() || spentValue.isBlank()) {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val priceValue = spentValue.toDoubleOrNull()
                    if (priceValue == null) {
                        Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (sharedViewModel != null) {
                        isLoading = true
                        val client = ApiContract.buildRetroFit()

                        // Update the SpentRequest with user input
                        sharedViewModel.currentSpentRequest?.let { request ->
                            request.name = spentName
                            request.price = priceValue

                            client.addSpent(sharedViewModel.token, request)
                                .enqueue(object : Callback<Account> {
                                    override fun onResponse(
                                        call: Call<Account>,
                                        response: Response<Account>
                                    ) {
                                        isLoading = false
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Gasto adicionado com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController?.navigate(Routes.GASTOS) {
                                                popUpTo(Routes.GASTOS) { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Erro ao adicionar gasto",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Account>, t: Throwable) {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Erro de conexão",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "ADICIONAR",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
