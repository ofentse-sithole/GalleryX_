package com.example.galleryx

class User (
            val username: String ="",
            val password: String = ""
){
    override fun toString(): String{
        return "$username  $password"
    }
}