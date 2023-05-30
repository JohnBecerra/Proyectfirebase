package com.example.practicafirebase

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profileImageUrl: String = ""
){
    constructor() : this("", "", "", "")
}