package jp.tomo0611.danime.ui.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DownloadsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Downloads Fragment"
    }
    val text: LiveData<String> = _text
}