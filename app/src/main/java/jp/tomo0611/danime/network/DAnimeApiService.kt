package jp.tomo0611.danime.network

import jp.tomo0611.danime.model.AnimeSearchResponse
import jp.tomo0611.danime.model.GetEpisodesRequest
import jp.tomo0611.danime.model.GetEpisodesResponse
import jp.tomo0611.danime.model.GetPlayParam
import jp.tomo0611.danime.model.GetRecentlyAiredEpisodesResponse
import jp.tomo0611.danime.model.GetRelatedContentsRequest
import jp.tomo0611.danime.model.GetRelatedContentsResponse
import jp.tomo0611.danime.model.GetTitleDetailRequest
import jp.tomo0611.danime.model.GetTitleDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DAnimeApiService {

    @Headers("Content-Type: application/json", "User-Agent: DOCOMO/2.0 Pixel 8(ASTB 2.0;44.00.10203;Android;14;34);locale:en-us;imei:00000;display:AP2A.240705.005;buildid:AP2A.240705.005;safeMode:OFF;webview:Chrome/127.0.6533.23")
    @POST("v1/WS600305")
    suspend fun getEpisodes(
        @Body body: List<GetEpisodesRequest>
    ): List<GetEpisodesResponse>

    @Headers("Content-Type: application/json", "User-Agent: DOCOMO/2.0 Pixel 8(ASTB 2.0;44.00.10203;Android;14;34);locale:en-us;imei:00000;display:AP2A.240705.005;buildid:AP2A.240705.005;safeMode:OFF;webview:Chrome/127.0.6533.23")
    @POST("v1/WS600303")
    suspend fun getTitleDetail(
        @Body body: List<GetTitleDetailRequest>
    ): List<GetTitleDetailResponse>

    @Headers("Content-Type: application/json", "User-Agent: DOCOMO/2.0 Pixel 8(ASTB 2.0;44.00.10203;Android;14;34);locale:en-us;imei:00000;display:AP2A.240705.005;buildid:AP2A.240705.005;safeMode:OFF;webview:Chrome/127.0.6533.23")
    @POST("v1/WS600308")
    suspend fun getRelatedContents(
        @Body body: List<GetRelatedContentsRequest>
    ): List<GetRelatedContentsResponse>

    // Original Function Name : getTvProgram
    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:130.0) Gecko/20100101 Firefox/130.0")
    @GET("WS000118?needScene=1&tvProgramFlag=1&vodTypeList=svod_tvod")
    suspend fun getRecentlyAiredEpisodes(
    ): GetRecentlyAiredEpisodesResponse

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:130.0) Gecko/20100101 Firefox/130.0")
    @GET("WS000107")
    suspend fun getGenreAnimeList(
        @Query("length") length: Int = 300,
        @Query("mainKeyVisualSize") mainKeyVisualSize: Int = 1,
        @Query("genreCd") genreCd: String = "11",
        @Query("vodTypeList") vodTypeList: String = "svod_tvod"
    ): AnimeSearchResponse

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:130.0) Gecko/20100101 Firefox/130.0")
    @GET("WS010105")
    suspend fun getPlayParam(
        @Query("viewType") viewType: Int = 5,
        @Query("partId") partId: String = "25335022",
        @Query("defaultPlay") defaultPlay: Int = 5,
        @Header("Cookie") cookie: String = "",
    ): GetPlayParam

}