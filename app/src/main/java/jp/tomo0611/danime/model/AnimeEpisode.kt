package jp.tomo0611.danime.model

data class AnimeEpisode(
    val videoEpisodeId: String,
    val episodeName: String,
    val episodeText: String,
    val thumbnailUrl: String,
    val isDownloaded: Int,
    val isAnimeSong: Int,
    val isHd: Int,
    val ageLimitType: Int
)

data class GetEpisodesRequest(
    val jsonrpc: String,
    val id: String,
    val method: String,
    val params: Params
)

data class Params(
    val videoTitleId: String,
    val sortType: Int
)

data class GetEpisodesResponse(
    val jsonrpc: String,
    val id: String,
    val error: String?,
    val result: Result
)

data class Result(
    val titleContents: TitleContents
)

data class TitleContents(
    val title: Title
)

data class Title(
    val videoTitleId: String,
    val episodeCount: String,
    val focusEpisodeId: String,
    val episodeContents: EpisodeContents
)

data class EpisodeContents(
    val sortType: Int,
    val episodeList: List<AnimeEpisode>
)