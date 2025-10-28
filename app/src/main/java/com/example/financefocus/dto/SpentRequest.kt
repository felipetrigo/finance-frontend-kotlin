package com.example.financefocus.dto;

data class SpentRequest(
    var clientUsername: String,
    var price: Double,
    var name: String
)
