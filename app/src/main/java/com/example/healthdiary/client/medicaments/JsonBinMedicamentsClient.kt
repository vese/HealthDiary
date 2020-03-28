package com.example.healthdiary.client.medicaments

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

const val secretKey = "\$2b\$10\$GSiw6WDJJOL.jO4YP.ubV.UnEF9IGUgjB55SyFOwntCX3hk0ei5ti"

interface JsonBinMedicamentsClient {

    @Headers("secret-key: $secretKey")
    @GET("b/5e7f897722c81b0ffa4d8230/2")
    fun getMedicaments(): Call<LinkedTreeMap<String, Any>>

    companion object {
        fun create(): JsonBinMedicamentsClient {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.jsonbin.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(JsonBinMedicamentsClient::class.java)
        }
    }
}