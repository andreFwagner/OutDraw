package at.andrewagner.outdraw.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * APIService to get all IDs of paintings in the collection of the met museum and get one by ID
 */

const val BASE_URL = "https://collectionapi.metmuseum.org/public/collection/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ArtApiService {

    @GET("v1/search?q=painting")
    suspend fun getAllArt(): AllArt

    @GET("v1/objects/{objectID}")
    suspend fun getArt(@Path("objectID") objectID: Int): ArtPiece
}

object ArtApi {
    val retrofitService: ArtApiService by lazy { retrofit.create(ArtApiService::class.java) }
}
