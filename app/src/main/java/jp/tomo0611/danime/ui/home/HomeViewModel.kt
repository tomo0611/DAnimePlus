package jp.tomo0611.danime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.tomo0611.danime.data.DAnimeRepository
import jp.tomo0611.danime.data.DefaultAppContainer
import jp.tomo0611.danime.model.GetRecentlyAiredEpisodesResponse
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    private val repository: DAnimeRepository = DefaultAppContainer().dAnimeRepository

    val result: LiveData<GetRecentlyAiredEpisodesResponse> = MutableLiveData()

    fun load() = viewModelScope.launch {
        val response = repository.getRecentlyAiredEpisodes()
        // set to result
        (result as MutableLiveData).postValue(response)
    }

    init {
        load()
    }
}