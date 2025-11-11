package com.example.financefocus.apicontract


import com.example.financefocus.dto.Account
import com.example.financefocus.dto.Spent
import com.example.financefocus.dto.SpentRequest
import com.example.financefocus.dto.Login;
import com.example.financefocus.dto.Sessao
import com.example.financefocus.dto.SpentUpdateRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiContract {
    @POST("v1/customer/save")
    fun createUser(@Body account: Account): Call<Account>

    @POST("v1/users/login")
    fun generateToken(@Body login: Login): Call<Sessao>

    @PUT("v1/customer/add/spent")
    fun addSpent(
        @Header("Authorization") token: String,
        @Body spentRequest: SpentRequest
    ): Call<Account>

    @GET("v1/spent/query")
    fun querySpents(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): Call<List<Spent>>

    @GET("v1/customer/get/{username}")
    fun queryCustomerData(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<Account>

    @DELETE("v1/spent/delete")
    fun deleteSpent(
        @Header("Authorization") token: String,
        @Query("id") spentId: Long
    ): Call<Void>

    @PUT("v1/spent/update")
    fun updateSpent(
        @Header("Authorization") token: String,
        @Body spent: SpentUpdateRequest
    ): Call<Spent>


    companion object {
        fun buildRetroFit(): ApiContract {
            return Retrofit
                .Builder()
                .baseUrl("https://finance-focus.yvx9rj7rmcs10.us-east-1.cs.amazonlightsail.com/finance-solution/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiContract::class.java)
        }
    }
}