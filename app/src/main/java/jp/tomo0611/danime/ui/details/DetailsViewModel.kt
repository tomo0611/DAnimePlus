package jp.tomo0611.danime.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.tomo0611.danime.data.DAnimeRepository
import jp.tomo0611.danime.data.DefaultAppContainer
import jp.tomo0611.danime.model.GetTitleDetailResponse
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Details Fragment"
    }
    val text: LiveData<String> = _text

    val result: LiveData<GetTitleDetailResponse> = MutableLiveData()

    private val repository: DAnimeRepository = DefaultAppContainer().dAnimeRepository

    fun setWorkId(workId: String) {
        _text.value = workId
        viewModelScope.launch {
            val response = repository.getTitleDetail(workId)
            (result as MutableLiveData).postValue(response)
        }
    }

}