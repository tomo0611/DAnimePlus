package jp.tomo0611.danime.model

data class AnimeSearchResponse(
    val resultCd: String = "00",
    val version: String = "1.5",
    val selfLink: String = "https://animestore.docomo.ne.jp/animestore/rest/WS000105?length=20&mainKeyVisualSize=2&sortKey=4&searchKey=%E7%84%A1%E8%81%B7%E8%BB%A2%E7%94%9F%E2%85%A1+%EF%BD%9E%E7%95%B0%E4%B8%96%E7%95%8C%E8%A1%8C%E3%81%A3%E3%81%9F%E3%82%89%E6%9C%AC%E6%B0%97%E3%81%A0%E3%81%99%EF%BD%9E&vodTypeList=svod_tvod&_=1721666747188",
    val data: AnimeSearchData
)

data class AnimeSearchData(
    val maxCount: Int = 1,
    val count: Int = 1,
    val workList: List<AnimeWork>
)

data class AnimeWork(
    val workId: String = "26424",
    val workInfo: AnimeWorkInfo,
    val seriesInfo: Any? = null
)

data class AnimeWorkInfo(
    val workTitle: String = "無職転生Ⅱ ～異世界行ったら本気だす～",
    val link: String = "https://animestore.docomo.ne.jp/animestore/ci_pc?workId=26424",
    val mainKeyVisualPath: String = "https://cs1.animestore.docomo.ne.jp/anime_kv/img/26/42/4/26424_1_2.png?1716440405048",
    val mainKeyVisualAlt: String = "無職転生Ⅱ ～異世界行ったら本気だす～_2",
    val workIcons: List<Any> = listOf(),
    val myListCount: Int = 163541,
    val favoriteCount: Int = 216403,
    val workTypeList: List<String> = listOf("anime"),
    val vodType: String = "svod",
    val ageLimitType: String = "0"
)