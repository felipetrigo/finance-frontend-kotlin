package com.example.financefocus.routes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financefocus.R
import com.example.financefocus.Routes
import com.example.financefocus.apicontract.ApiContract
import com.example.financefocus.dto.Login
import com.example.financefocus.dto.Sessao
import com.example.financefocus.dto.SharedViewModel
import com.seuapp.financefocus.ui.theme.FinanceFocusTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController,sharedViewModel: SharedViewModel?) {
    var client = ApiContract.buildRetroFit()
    var username by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }

    val passwordRef = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    // AlertDialog para mostrar erros
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Erro \uD83D\uDE25") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0052CC))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Adicionando a logo (substitua R.drawable.logo pelo seu recurso real)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Finance Focus",
            modifier = Modifier
                .height(250.dp)
                .padding(bottom = 32.dp)
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
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
                    passwordRef.requestFocus()
                }
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordRef)
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                isClicked = true
                client.generateToken(Login(username,senha)).enqueue(object: Callback<Sessao>{
                    override fun onResponse(call: Call<Sessao>, response: Response<Sessao>) {
                        Log.println(Log.ASSERT,"response client",response.toString())
                        Log.println(Log.ASSERT, "body ",response.body().toString())


                        if (response.isSuccessful) {
                            sharedViewModel?.username = username
                            sharedViewModel?.password = senha
                            sharedViewModel?.token = response.body()?.token?:""
                            navController.navigate(Routes.GASTOS)
                        } else {
                            isClicked = false
                            errorMessage = when (response.code()) {
                                401 -> "Usuário ou senha incorretos"
                                500 -> "Usuário ou senha incorretos"
                                else -> "Usuário ou senha incorretos"
                            }
                            showErrorDialog = true
                        }
                    }
                    override fun onFailure(call: Call<Sessao>, t: Throwable) {
                        isClicked = false
                        Log.println(Log.ERROR,"response client",t.toString())
                        errorMessage = when {
                            t.message?.contains("Unable to resolve host") == true ->
                                "Erro de conexão"
                            t.message?.contains("timeout") == true ->
                                "Erro de conexão"
                            else -> "Erro de conexão"
                        }
                        showErrorDialog = true
                    }
                })

                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isClicked,
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            if(isClicked){
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp))
            }else{
                Text("Entrar", color = Color.Black)
            }
        }
        Button(
            onClick = {
                navController.navigate(Routes.START)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(16.dp)
        ){
            Text("Voltar", color = Color.Black)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    FinanceFocusTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0052CC)
        ) {
            LoginScreen(navController = rememberNavController(), sharedViewModel = null)
        }
    }
}