package jp.tomo0611.danime.ui.player

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import jp.tomo0611.danime.data.DAnimeRepository
import jp.tomo0611.danime.data.DefaultAppContainer
import jp.tomo0611.danime.model.GetPlayParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class PlayerViewModel : ViewModel() {

    private val repository: DAnimeRepository = DefaultAppContainer().dAnimeRepository

    val result_token: LiveData<String> = MutableLiveData()
    val result: LiveData<GetPlayParam> = MutableLiveData()

    private suspend fun getManifestKIDAndLicense(url: String, oneTimeKey: String): String =
        withContext(
            Dispatchers.IO
        ) {
            val request: Request = Request.Builder().url(url).build()
            val okHttpClient = OkHttpClient()

            // How to avoid NetworkOnMainThreadException?
            val body: String = okHttpClient.newCall(request).execute().body()?.string() ?: ""
            Log.d("body", body)
            var KID = ""
            val matcher = Pattern.compile("default_KID=\".+?\"").matcher(body)
            while (matcher.find()) {
                Log.d("KID", body.substring(matcher.start(), matcher.end()))
                KID = body.substring(matcher.start(), matcher.end()).substring(13)
                KID = KID.substring(0, KID.length - 1)
            }
            if (KID.isNotEmpty()) {
                return@withContext getLicenseAndPlay(KID, oneTimeKey)
            }
            return@withContext ""
        }

    private suspend fun getLicenseAndPlay(KID: String, oneTimeKey: String): String = withContext(
        Dispatchers.IO
    ) {
        try {
            val getUrl =
                "https://wv.anime.dmkt-sp.jp/RequestLicense/Tokens/?&keyId=" + KID + "&oneTimeKey=" + oneTimeKey
            val url = URL(getUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val `in` = connection.inputStream
                var encoding = connection.contentEncoding
                if (null == encoding) {
                    encoding = "UTF-8"
                }
                val result = StringBuffer()
                val inReader = InputStreamReader(`in`, encoding)
                val bufReader = BufferedReader(inReader)
                var line: String? = null
                while (bufReader.readLine().also { line = it } != null) {
                    result.append(line)
                }
                bufReader.close()
                inReader.close()
                `in`.close()
                Log.d("result", result.toString())
                val jo = JSONObject(result.toString())
                val token = jo.getString("tokenInfo")
                return@withContext token
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext ""
        }

        return@withContext ""
    }

    fun load(episodeId: String, context: PlayerActivity) {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(
                context
            )

        val dma_user = sharedPreferences.getString("dma_user", "")
        val certificate_session_id = sharedPreferences.getString("certificate_session_id", "")
        if (dma_user == "" || certificate_session_id == "") {
            Log.d("error", "dma_user or certificate_session_id is empty")
            return
        }

        viewModelScope.launch {
            val response = repository.getPlayParam(episodeId, "certificate_session_id=$certificate_session_id; dma_user=$dma_user")
            if (response.data !== null) {
                val token = getManifestKIDAndLicense(
                    response.data.castContentUri.replace(
                        "http://",
                        "https://"
                    ), response.data.oneTimeKey
                )
                if (token == "") {
                    Log.d("token", "error")
                }
                (result_token as MutableLiveData).postValue(token)
            } else {
                Log.d("response", response.toString())
            }
            (result as MutableLiveData).postValue(response)
        }
    }
}