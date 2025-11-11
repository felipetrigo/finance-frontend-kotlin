package com.example.financefocus.dto

data class SpentUpdateRequest (
    var id: Long,
    var username: String,
    var name: String,
    var price: Double
)