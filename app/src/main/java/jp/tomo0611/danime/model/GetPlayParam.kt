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

// エラーメッセージ（WS010105）
const val ERROR_MSG_WS010105_22 = "この話は現在公開されておりません"
const val ERROR_MSG_WS010105_23 = "このコンテンツは現在視聴できません"
const val ERROR_MSG_WS010105_24 = "この作品はご利用の端末で現在視聴いただけません"
const val ERROR_MSG_WS010105_25 = "この作品はご利用の端末で現在視聴いただけません"
const val ERROR_MSG_WS010105_26 = "同一dアカウントによる複数端末での動画視聴はできません"
const val ERROR_MSG_WS010105_27 = "この作品はレンタル作品です。レンタル後にお楽しみいただけます"
const val ERROR_MSG_WS010105_28 = "この作品はレンタル作品です。ご利用の端末からのレンタルはできません"
const val ERROR_MSG_WS010105_29 = "視聴数が上限に達しているため、視聴できません"
const val ERROR_MSG_WS010105_30 = "このコンテンツは当選者のみ視聴可能です"
const val ERROR_MSG_WS010105_31 = "このコンテンツはキャンペーンコード入力後にご視聴頂けます"
const val ERROR_MSG_WS010105_32 = "入力されたキャンペーンコードではこの動画を視聴できません"
const val ERROR_MSG_WS010105_33 = "習熟IDのため視聴できません"
const val ERROR_MSG_WS010105_36 = "お客様の年齢では本作品は視聴できません。"
const val ERROR_MSG_WS010105_37 = "利用者情報の提供が拒否されているため、年齢制限のある作品は視聴できません。"
const val ERROR_MSG_WS010105_38 = "契約者による利用者情報拒否設定がされているため、年齢制限のある作品は視聴できません。"
const val ERROR_MSG_WS010105_39 = "お客様の年齢が確認できませんでした。<br>しばらく待ってから再度お試しください。"
const val ERROR_MSG_WS010105_81 = "海外からのアクセスです\n日本国内でのみ利用可能なサービスとなります"
const val ERROR_MSG_WS010105_86 = "会員専用のコンテンツとなります\nログイン後お楽しみください"