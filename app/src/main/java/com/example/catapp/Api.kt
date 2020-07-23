package com.example.catapp

import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.withContext

interface CatsApi {
    @GET("v1/images/search")
    suspend fun getListOfCats(): CatData
}

object CatsApiImpl {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.thecatapi.com/")
        .build()

    private val catsService = retrofit.create(CatsApi::class.java)

    suspend fun getListOfCats(): List<CatDataItem> {
        val list = ArrayList<CatDataItem>()
        repeat(10) {
            withContext(Dispatchers.IO) {
                catsService.getListOfCats()
                    .map { it ->
                        list.add(
                            CatDataItem(
                                it.breeds,
                                it.height,
                                it.id,
                                it.url,
                                it.width
                            )
                        )
                    }
            }
        }

        return list
    }
}