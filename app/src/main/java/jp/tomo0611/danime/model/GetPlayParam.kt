package jp.tomo0611.danime.model

data class GetPlayParam(
    val resultCd: String,
    val version: String,
    val selfLink: String,
    val data: GetPlayParamData
)

data class GetPlayParamData(
    val partId: String,
    val workTitle: String,
    val mainKeyVisualPath: String,
    val partDispNumber: String,
    val partExp: String,
    val partTitle: String,
    val partMeasureSecond: Int,
    val mainScenePath: String,
    val serviceId: String,
    val oneTimeKey: String,
    val viewOneTimeToken: String,
    val title: String,
    val webInitiatorUri: String,
    val defaultPlay: String,
    val laUrl: String,
    val castCustomDataUrl: String,
    val castOneTimeKey: String,
    val castContentUri: String,
    val apiUrl: String
)