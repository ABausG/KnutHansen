package antonborri.es.knuthansen

import android.app.Application
import android.graphics.*
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import javax.inject.Inject


class ImageDrawer @Inject constructor(var app: Application) {


    private val cm = ColorMatrix().apply {
        setSaturation(0f)
    }

    private val filter = ColorMatrixColorFilter(cm);

    private val eyePaint = Paint().apply {
        //alpha = 160
        color = ContextCompat.getColor(app, R.color.knutBlue)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
    }

    private val anchorPaint = Paint().apply {
        alpha = 160
        color = Color.BLACK
        xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
    }


    private val imagePaint = Paint().apply {
        setColorFilter(filter)
    }


    fun drawFaces(uri: Uri, faces: List<FirebaseVisionFace>): Bitmap {
        val dimensions = getDimensions(uri)
        val bmp = Bitmap.createBitmap(dimensions.first, dimensions.second, Bitmap.Config.RGB_565)
        val canvas = Canvas(bmp)

        val image = BitmapFactory.decodeStream(app.contentResolver.openInputStream(uri))

        canvas.drawBitmap(image, 0f, 0f, imagePaint)

        for (face in faces) {
            val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)
            val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)

            val rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK)

            val radius = face.boundingBox.width() / 35f

            drawEye(canvas, rightEye, radius)
            drawEye(canvas, leftEye, radius)

            drawAnchor(canvas, rightCheek, radius)
            //canvas.drawRect(face.boundingBox, eyePaint)
        }
        return bmp
    }

    private fun drawEye(canvas: Canvas, eye: FirebaseVisionFaceLandmark?, radius: Float) {
        if (eye != null) {
            canvas.drawCircle(eye.position.x, eye.position.y, radius, eyePaint)
        }
    }

    private fun drawAnchor(canvas: Canvas, cheek: FirebaseVisionFaceLandmark?, size: Float) {
        if(cheek != null) {
            val drawable = app.resources.getDrawable(R.drawable.ic_anchor)
            drawable.setBounds((cheek.position.x-size).toInt(), (cheek.position.y-size).toInt(), (cheek.position.x+size).toInt(), (cheek.position.y+size).toInt())
            drawable.draw(canvas)
        }
    }

    private fun getDimensions(uri: Uri): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(app.contentResolver.openInputStream(uri), null, options)
        return Pair(options.outWidth, options.outHeight)
    }

}