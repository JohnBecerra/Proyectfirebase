package com.example.practicafirebase

import java.util.*

data class Mantenimiento(
    val tecnico: String,
    val establecimiento: String,
    val fecha: Date,
    val observaciones: String,
    val profileImageUrl: String
) {
    constructor() : this("", "", Date(), "", "")
}
