package com.example.budgetshield.data

import java.io.Serializable
data class RecordModel(
    val type: String,
    val date: String,
    val description: String,
    val amount: String
) : Serializable
