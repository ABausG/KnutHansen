package antonborri.es.knuthansen


import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_process.*
import javax.inject.Inject
import kotlin.math.E


class ProcessFragment : Fragment() {


    @Inject
    lateinit var faceDetector: FirebaseVisionFaceDetector

    @Inject
    lateinit var imageDrawer: ImageDrawer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_process, container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri = Uri.parse(arguments?.getString("photo"))


        if (context != null) {
            val fvi = FirebaseVisionImage.fromFilePath(context!!, uri)
            val result = faceDetector.detectInImage(fvi)
            result.addOnSuccessListener {
                for (face in it) {
                    Log.i("RIGHT_EYE", face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)?.position.toString())
                    Log.i("LEFT_EYE", face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)?.position.toString())
                }
                img_photo.setImageBitmap(imageDrawer.drawFaces(uri, it))

            }.addOnFailureListener {
                Log.e("FACE FAILURE", it.message)
            }
        }
    }

}
