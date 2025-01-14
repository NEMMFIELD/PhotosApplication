package com.example.authorization.domain

interface PhotoAuthRepository {
    suspend fun getAccessToken(code:String):String
    fun saveToken(token:String)
    fun getToken():String?

}
