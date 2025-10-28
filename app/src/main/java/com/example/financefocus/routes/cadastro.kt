package com.example.financefocus.routes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.financefocus.dto.Account
import com.example.financefocus.dto.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CadastroScreen(navController: NavController,sharedViewModel: SharedViewModel?) {
    var client = ApiContract.buildRetroFit()
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") } //campos vazios
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val nameRef = remember { FocusRequester() }
    val usernameRef = remember { FocusRequester() }
    val passwordRef = remember { FocusRequester() }
    val emailRef = remember { FocusRequester() }
    val salaryRef = remember { FocusRequester() }
    val phoneRef = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()

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
            .verticalScroll(scrollState)
            .background(Color(0xFF0052CC))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Finance Focus",
            modifier = Modifier
                .height(200.dp)
                .padding(bottom = 32.dp)
        )

        // Título
        Text(
            text = "Criar Nova Conta",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo Nome
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move o foco para o próximo campo
                    usernameRef.requestFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(nameRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Campo Usuário
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(usernameRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Campo Senha
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move o foco para o próximo campo
                    emailRef.requestFocus()
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(passwordRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Campo E-mail
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move o foco para o próximo campo
                    salaryRef.requestFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(emailRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Campo Salário
        OutlinedTextField(
            value = salary,
            onValueChange = { salary = it },
            label = { Text("Salário (R$)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move o foco para o próximo campo
                    phoneRef.requestFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(salaryRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Campo Telefone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefone") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done // Define a ação como "Próximo"
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .focusRequester(phoneRef)
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = textFieldColors()
        )

        // Botão Criar Conta
        Button(
            onClick = {
                isClicked = true
                var elements = listOf(name,username,password,salary,phone,email)
                var indicator = false
                elements.forEach { element ->
                    if(element.isEmpty()){
                        indicator = true
                    }
                }
                if(indicator){
                    isClicked = false
                    errorMessage = "algum campo não foi inputado corretamente"
                    showErrorDialog = true
                }else{
                    val account = Account(name,username,password,salary.toDouble(),phone,email)
                    client.createUser(account).enqueue(object : Callback<Account>{
                        override fun onResponse(call: Call<Account>, response: Response<Account>) {
                            Log.println(Log.ASSERT,"response client",response.toString())
                            Log.println(Log.ASSERT, "body ",response.body().toString())


                            if (response.isSuccessful) {
                                sharedViewModel?.username = username
                                sharedViewModel?.password = password
                                sharedViewModel?.generateToken()
                                navController.navigate(Routes.GASTOS) // Navega após cadastro
                            } else {
                                isClicked = false
                                errorMessage = when (response.code()) {
                                    401 -> "erro ao contactar o servidor"
                                    500 -> "erro, não foi possivel "
                                    else -> "erro desconhecido"
                                }
                                showErrorDialog = true
                            }
                        }

                        override fun onFailure(call: Call<Account>, t: Throwable) {
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
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isClicked,
            colors = ButtonDefaults.buttonColors(Color.White,Color.Black),
            shape = RoundedCornerShape(16.dp)
        ) {
            if(isClicked){
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp))
            }else {
                Text("Criar Conta", style = MaterialTheme.typography.labelLarge)
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

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedLabelColor = Color.Gray,
    unfocusedLabelColor = Color.Gray,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    cursorColor = Color.Black
)

@Preview(showBackground = true)
@Composable
fun CadastroScreenPreview() {
    CadastroScreen(navController = rememberNavController(), sharedViewModel = null)
}
