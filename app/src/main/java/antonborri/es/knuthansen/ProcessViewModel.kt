package antonborri.es.knuthansen

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import javax.inject.Inject

class ProcessViewModel @Inject constructor(val drawer: ImageDrawer) : ViewModel() {

    companion object {
        const val ZOOM_FACTOR = 0.05f
        const val MOVE_FACTOR = 5
    }

    val currentFeature : LiveData<FeatureType> = MutableLiveData()

    init {
        (currentFeature as MutableLiveData).postValue(FeatureType.RIGHT_EYE)
    }

    val image = drawer.getImage()

    fun setFaces(uri : Uri, faces : List<FirebaseVisionFace>) = drawer.setFaces(uri, faces)

    fun zoomFeature(direction : Int) = drawer.changeSize(direction * ZOOM_FACTOR, currentFeature.value ?: FeatureType.RIGHT_EYE)

    fun moveFeature(direction: Direction) = drawer.moveFeature(MOVE_FACTOR, currentFeature.value ?: FeatureType.RIGHT_EYE, direction)

    fun setFeature(featureType: FeatureType) = (currentFeature as MutableLiveData).postValue(featureType)
}