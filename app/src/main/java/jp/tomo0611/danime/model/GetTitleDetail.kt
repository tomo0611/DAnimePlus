package jp.tomo0611.danime.model

data class GetTitleDetailRequest(
    val jsonrpc: String = "2.0",
    val id: String = "0000",
    val method: String = "title",
    val params: GetTitleParams
)

data class GetTitleParams(
    val videoTitleId: String
)

data class GetTitleDetailResponse(
    val jsonrpc: String = "2.0",
    val id: Int = 25448,
    val error: Any? = null,
    val result: AnimeTitleResult
)

data class AnimeTitleResult(
    val titleContents: AnimeTitleContents
)

data class AnimeTitleContents(
    val title: AnimeTitle
)

data class AnimeTitle(
    val videoTitleId: Int,
    val titleName: String ,
    val titleNameUpper: String,
    val titleNameLower: String = "2022年",
    val indexImageUrl: String,
    val titleImageUrl: String,
    val runningTime: Any? = null,
    val isDownloaded: Int = 0,
    val isAnimeSong: Int = 0,
    val fav: Int = 0,
    val deliverType: Int = 1,
    val titleDetail: String = "HTMLText",
    val episodeContents: Any? = null,
    val ageLimitType: Int = 0,
    val quality: Int = 5
)