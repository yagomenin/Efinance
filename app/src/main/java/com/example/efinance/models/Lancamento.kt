package com.example.efinance.models

data class Lancamento (
    var _id: Int,
    var tipo : String,
    var detalhe: String,
    var data_lanc: String,
    var valor: Double
)