package jp.tomo0611.danime.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.tomo0611.danime.data.DAnimeRepository
import jp.tomo0611.danime.data.DefaultAppContainer
import jp.tomo0611.danime.model.AnimeSearchResponse
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val result: LiveData<AnimeSearchResponse> = MutableLiveData()

    private val repository: DAnimeRepository = DefaultAppContainer().dAnimeRepository

    fun setGenreId(genreId: String) {
        viewModelScope.launch {
            val response = repository.getGenreAnimeList(genreId)
            (result as MutableLiveData).postValue(response)
        }
    }

}