package jp.tomo0611.danime.model

data class GetRelatedContentsRequest(
    val jsonrpc: String,
    val id: String,
    val method: String,
    val params: GetRelatedContentsRequestParams
)

data class GetRelatedContentsRequestParams(
    val videoTitleId: String
)

data class GetRelatedContentsResponse(
    val jsonrpc: String = "2.0",
    val id: String = "0000",
    val error: Any? = null,
    val result: RelatedContentsResult
)

data class RelatedContentsResult(
    val relatedContentsCategory: List<RelatedContentsCategory>
)

data class RelatedContentsCategory(
    val categoryName: String = "シリーズ／関連のアニメ作品",
    val relatedContents: List<RelatedContent>
)

data class RelatedContent(
    val videoTitleId: String = "23750",
    val contentType: String = "title",
    val videoTitleName: String = "かぐや様は告らせたい？～天才たちの恋愛頭脳戦～",
    val titleThumbnail720pUrl: String? = null,
    val titleThumbnailUrl: String = "https://cs1.animestore.docomo.ne.jp/anime_kv/img/23/75/0/23750_1_1.png?1583985633000",
    val episodeId: Int? = null,
    val episodeText: String? = null,
    val episodeTitleName: String? = null,
    val isDownloaded: Int = 0,
    val isAnimeSong: Int = 0,
    val isHd: Int = 1,
    val ageLimitType: Int = 0
)