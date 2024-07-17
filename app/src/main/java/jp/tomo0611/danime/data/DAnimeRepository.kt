package jp.tomo0611.danime.data

import jp.tomo0611.danime.model.GetEpisodesRequest
import jp.tomo0611.danime.model.GetEpisodesResponse
import jp.tomo0611.danime.model.GetRecentlyAiredEpisodesResponse
import jp.tomo0611.danime.model.GetTitleDetailRequest
import jp.tomo0611.danime.model.GetTitleDetailResponse
import jp.tomo0611.danime.model.GetTitleParams
import jp.tomo0611.danime.model.Params
import jp.tomo0611.danime.network.DAnimeApiService

interface DAnimeRepository {
    suspend fun getRecentlyAiredEpisodes(): GetRecentlyAiredEpisodesResponse
    suspend fun getTitleDetail(workId: String): GetTitleDetailResponse
    suspend fun getEpisodes(workId: String): GetEpisodesResponse
}

class NetworkDAnimeRepository(
    private val dAnimeApiService: DAnimeApiService
) : DAnimeRepository {
    override suspend fun getRecentlyAiredEpisodes(): GetRecentlyAiredEpisodesResponse =
        dAnimeApiService.getRecentlyAiredEpisodes()

    override suspend fun getTitleDetail(
        workId: String
    ): GetTitleDetailResponse = dAnimeApiService.getTitleDetail(
        listOf(
            GetTitleDetailRequest(
                jsonrpc = "2.0", id = "0000", method = "title",
                params = GetTitleParams(videoTitleId = workId)
            )
        )
    )[0]

    override suspend fun getEpisodes(workId: String): GetEpisodesResponse =
        dAnimeApiService.getEpisodes(
            listOf(
                GetEpisodesRequest(
                    jsonrpc = "2.0", id = "0000", method = "episodeList",
                    params = Params(videoTitleId = workId, sortType = 4)
                )
            )
        )[0]
}