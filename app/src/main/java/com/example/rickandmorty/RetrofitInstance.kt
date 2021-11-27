package com.example.rickandmorty

import com.example.rickandmorty.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * creating a singleton of retrofit within an object class (called singleton class in java which has static
 * fields and methods) by building to retrofit instance with the help of OkHttpClient
 * which is responsible for handling the request and the response of a particular communication
 * between the client and the server.
 * [GsonConverterFactory] is used to serialize and deserialize our object
 * in order to transfer from and to the internet
 *
 */

object RetrofitInstance {
    private val retrofit by lazy {

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logger).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * Passing our [RickAndMortyAPi] interface to Retrofit create method which returns an instance
     * of it in order to create the implementation of it
     */

    val api: RickAndMortyAPi = retrofit.create(RickAndMortyAPi::class.java)

}