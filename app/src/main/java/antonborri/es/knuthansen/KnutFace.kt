package antonborri.es.knuthansen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark

class KnutFace(face: FirebaseVisionFace) {

    var baseSize : Float = 0f

    lateinit var rightEyePosition : Point
    var rightEyeSize : Float = 1f

    lateinit var leftEyePosition : Point
    var leftEyeSize : Float = 1f

    lateinit var anchorPosition : Point
    var anchorSize : Float = 1f

    init {
        baseSize = face.boundingBox.width() / 35f

        val right = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)
        if(right != null){
            rightEyePosition = Point(right.position.x.toInt(), right.position.y.toInt())
        }

        val left = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
        if(left != null){
            leftEyePosition = Point(left.position.x.toInt(), left.position.y.toInt())
        }

        val cheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK)
        if(cheek != null){
            anchorPosition = Point(cheek.position.x.toInt(), cheek.position.y.toInt())
        }
    }

}