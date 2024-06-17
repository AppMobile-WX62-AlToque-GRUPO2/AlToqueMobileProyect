package com.example.altoque.networking

import com.example.altoque.models.Login
import com.example.altoque.models.Register
import com.example.altoque.models.TokenLogin
import com.example.altoque.models.VTokenData
import com.example.altoque.models.VerifyToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AltoqueService {

    @POST("/auth/login")
    fun postLogin(@Body login: Login): Call<Login>

    @POST("/auth/register")
    fun postRegister(@Body register: Register): Call<Register>

    @GET("/auth/token_info")
    fun getTokenInfo(): Call<TokenLogin>

//    @POST("/auth/verify/token")
//    fun postVerifyToken(@Body verifyToken: VerifyToken): Call<VerifyToken>
//
//    @GET("/auth/user/data")
//    fun getUserDataInfo(): Call<VTokenData>

    @POST("/auth/verificar_token")
    fun postToken(@Body login: Login): Call<Login>

    @GET("/auth/verificar_token")
    fun getDataInf(): Call<VTokenData>

}