package com.example.rickandmorty

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * A service interface which is implemented by [Retrofit.create] method at runtime
 * which defines the end point to the API by specifying a route or a parameter
 * to that end point.
 * In order to specify that, we annotate our function with the built-in Retrofit annotations
 * in order to specify the kind of request
 */


interface RickAndMortyAPi {

    /**
     * returns a random character object from our API by specify a number coerce in 1 to 826
     * @param id the id used to get a specific character object
     * @return Response of the type of our character object
     */

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int) : Response<Rick>
}