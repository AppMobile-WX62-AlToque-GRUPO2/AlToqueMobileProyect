package com.example.altoque.models

data class Post (
    val title : String,
    val description : String,
    val address : String,
    val image : String,
    val is_publish : Boolean,
    val clientId : Int
)

data class PostResponse (
    val id : Int,
    val title : String,
    val description : String,
    val address : String,
    val image : String,
    val is_publish : Boolean,
    val clientId : Int
)