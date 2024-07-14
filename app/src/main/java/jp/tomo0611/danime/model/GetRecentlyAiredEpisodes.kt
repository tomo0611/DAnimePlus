package jp.tomo0611.danime.model

data class GetRecentlyAiredEpisodesResponse(
    val data: WorkListData,
    val resultCd: String,
    val version: String,
    val selfLink: String
)

data class WorkListData(
    val workList: List<Work>,
    val cacheExpirationDate: String
)

data class Work(
    val workId: String,
    val workInfo: WorkInfo
)

data class WorkInfo(
    val workTitle: String,
    val link: String,
    val mainKeyVisualPath: String,
    val mainKeyVisualAlt: String,
    val workIcons: List<String>,
    val workIconInfoList: List<Int>,
    val myListCount: Int,
    val favoriteCount: Int,
    val workWeek: String,
    val workTime: String,
    val cours: String,
    val tvProgramFlag: String,
    val vodType: String,
    val ageLimitType: String,
    val startPartDispNumber: String,
    val endPartDispNumber: String?,
    val latestPartPublicDate: String,
    val scenes: List<Scene>
)

data class Scene(
    val link: String,
    val mainScenePath: String,
    val mainSceneAlt: String
)