package at.andrewagner.outdraw.gallery

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.android.outdraw.R
import at.andrewagner.outdraw.base.BaseViewModel
import at.andrewagner.outdraw.base.NavigationCommand
import at.andrewagner.outdraw.database.ArtPieceData
import at.andrewagner.outdraw.database.Painting
import at.andrewagner.outdraw.network.ArtApi
import at.andrewagner.outdraw.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * GalleryViewModel with all the Logic of the Home, Gallery, Detail and Painting Screen
 */

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

    private val _artPiece = MutableLiveData<ArtPieceData?>()
    val artPiece: LiveData<ArtPieceData?>
        get() = _artPiece

    init {
        loadPaintings()
        loadSaveArt()
    }

    private fun loadSaveArt() {
        viewModelScope.launch {
            try {
                val allArt = ArtApi.retrofitService.getAllArt()
                allArt.let { it1 ->
                    val index = Random.nextInt(0, it1.objectIDs.size)
                    val piece = ArtApi.retrofitService.getArt(it1.objectIDs[index])
                    piece.let { it2 ->
                        val imgUri = it2.primaryImage.toUri().buildUpon().scheme("https").build()
                        repository.saveArtPiece(ArtPieceData(1, it2.title, loadArt(imgUri, it2.title)))
                        _artPiece.value = repository.updateArtPiece()
                    }
                }
            } catch (e: Exception) {
                _artPiece.value = repository.updateArtPiece()
            }
        }
    }

    private suspend fun loadArt(uri: Uri, title: String): String {
        val file = File(app.getExternalFilesDir(null).toString() + "/artPiece-$title.jpg")
        withContext(Dispatchers.IO) {
            val imageBitmap = Glide.with(app)
                .asBitmap()
                .load(uri)
                .submit()
                .get()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
        }
        return file.absolutePath
    }

    @SuppressLint("SimpleDateFormat")
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
