package com.example.financefocus.dto

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financefocus.apicontract.ApiContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var token by mutableStateOf("")
    private var client = ApiContract.buildRetroFit()
    fun setLoginData(user: String, password: String) {
        this.username = user
        this.password = password
    }
    fun generateToken(){
        client.generateToken(Login(username,password)).enqueue(object: Callback<Sessao> {
            override fun onResponse(call: Call<Sessao>, response: Response<Sessao>) {
                token = response.body()?.token.toString()
                Log.println(Log.ASSERT,"token",token)
            }

            override fun onFailure(call: Call<Sessao>, t: Throwable) {
                Log.println(Log.ASSERT,"erro",t.message.toString())
            }
        })
    }
}