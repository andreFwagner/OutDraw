package com.example.android.outdraw.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting
import com.example.android.outdraw.repository.Repository
import com.udacity.project4.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(val app: Application, private val repository: Repository) : BaseViewModel(app) {

    var paintingsList = MutableLiveData<List<Painting>>()

    init {
        loadPaintings()
    }

    fun loadPaintings() {
        viewModelScope.launch {
            paintingsList.value = repository.loadPaintings()
        }
    }

    fun savePainting(path: String) {
        val format = SimpleDateFormat("yyy-M-dd")
        val date = format.format(Date())
        val painting = Painting(0, date.toString(), path)
        viewModelScope.launch {
            repository.savePainting(painting)
        }
        showToast.value = app.getString(R.string.painting_saved)
    }

//    fun showWork() {
//        val test = paintingsList.value?.first()?.image
//        Log.e("FLOW", test!!)
//    }
}
