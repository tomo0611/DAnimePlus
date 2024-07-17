package jp.tomo0611.danime.data

import jp.tomo0611.danime.model.GetRecentlyAiredEpisodesResponse
import jp.tomo0611.danime.network.DAnimeApiService

interface DAnimeRepository {
    suspend fun getRecentlyAiredEpisodes(): GetRecentlyAiredEpisodesResponse
}

class NetworkDAnimeRepository(
    private val dAnimeApiService: DAnimeApiService
) : DAnimeRepository {
    override suspend fun getRecentlyAiredEpisodes(): GetRecentlyAiredEpisodesResponse = dAnimeApiService.getRecentlyAiredEpisodes()
}