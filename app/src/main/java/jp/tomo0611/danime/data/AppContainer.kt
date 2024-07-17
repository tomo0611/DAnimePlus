package jp.tomo0611.danime.data

import retrofit2.Retrofit
import jp.tomo0611.danime.network.DAnimeApiService
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val dAnimeRepository: DAnimeRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://animestore.docomo.ne.jp/animestore/rest/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        //.client(client)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: DAnimeApiService by lazy {
        retrofit.create(DAnimeApiService::class.java)
    }

    /**
     * DI implementation for Mars photos repository
     */
    override val dAnimeRepository: DAnimeRepository by lazy {
        NetworkDAnimeRepository(retrofitService)
    }
}