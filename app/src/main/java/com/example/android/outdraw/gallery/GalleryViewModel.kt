package com.example.android.outdraw.gallery

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting
import com.example.android.outdraw.repository.Repository
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GalleryViewModel(val app: Application, private val repository: Repository) : BaseViewModel(app) {

    private val _paintingsList = MutableLiveData<List<Painting>>()
    val paintingsList: LiveData<List<Painting>>
        get() = _paintingsList

    private val _navigateToSelectedPainting = MutableLiveData<Painting?>()
    val navigateToSelectedPainting: LiveData<Painting?>
        get() = _navigateToSelectedPainting

    private val _selectedPainting = MutableLiveData<Painting>()
    val selectedPainting: LiveData<Painting>
        get() = _selectedPainting


    init {
        loadPaintings()
    }


    fun savePainting(path: String) {
        val format = SimpleDateFormat("yyy-M-dd")
        val date = format.format(Date())
        val painting = Painting(0, date.toString(), path)
        viewModelScope.launch {
            repository.savePainting(painting)
            showToast.value = app.getString(R.string.painting_saved)
        }
    }

    fun setSelectedPainting(painting: Painting) {
        _selectedPainting.value = painting
    }

    private fun loadPaintings() {
        viewModelScope.launch {
            _paintingsList.value = repository.loadPaintings()
        }
    }

    fun getPaintingDetail(painting: Painting) {
        _navigateToSelectedPainting.value = painting
    }

    fun displayPaintingDetailComplete() {
        _navigateToSelectedPainting.value = null
    }

    fun deletePainting(painting: Painting) {
        runBlocking {
            File(painting.image).delete()
            repository.deletePainting(painting)
            _paintingsList.value = repository.loadPaintings()
            showToast.value = app.getString(R.string.painting_deleted)
            navigationCommand.postValue(NavigationCommand.To(DetailFragmentDirections.actionDetailFragmentToGalleryFragment()))
        }
    }
}
