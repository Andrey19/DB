package ru.effectivemobile.db.network


import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("posts/1")
    suspend fun getPost(): Response<Post>
}

data class Post(val id: Int, val title: String)