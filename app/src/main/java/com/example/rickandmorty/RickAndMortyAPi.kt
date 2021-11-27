package com.example.rickandmorty

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface RickAndMortyAPi {
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int) : Response<Rick>
}