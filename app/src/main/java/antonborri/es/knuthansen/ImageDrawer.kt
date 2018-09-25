package antonborri.es.knuthansen

import android.app.Application
import android.graphics.*
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import javax.inject.Inject


class ImageDrawer @Inject constructor(var app: Application) {

    private val data: MutableLiveData<Bitmap> = MutableLiveData()

    private val cm = ColorMatrix().apply {
        setSaturation(0f)
    }

    private val filter = ColorMatrixColorFilter(cm);

    private val eyePaint = Paint().apply {
        alpha = 160
        color = ContextCompat.getColor(app, R.color.eyeColor)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    }

    private val pupilPaint = Paint().apply {
        alpha = 90
        color = Color.BLACK
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

    fun getImage(): LiveData<Bitmap> = data

    private var knutFaces: MutableList<KnutFace> = ArrayList()
    private lateinit var uri: Uri

    fun setFaces(uri: Uri, faces: List<FirebaseVisionFace>) {
        this.uri = uri
        knutFaces = ArrayList()
        for (face in faces) {
            knutFaces.add(KnutFace(face))
        }
        drawFaces()
    }

    private fun drawFaces() {

        val dimensions = getDimensions(uri)
        val bmp = Bitmap.createBitmap(dimensions.first, dimensions.second, Bitmap.Config.RGB_565)
        val canvas = Canvas(bmp)

        val image = BitmapFactory.decodeStream(app.contentResolver.openInputStream(uri))

        canvas.drawBitmap(image, 0f, 0f, imagePaint)

        for (face in knutFaces) {
            drawEye(canvas, face.rightEyePosition, face.baseSize * face.rightEyeSize)
            drawEye(canvas, face.leftEyePosition, face.baseSize * face.leftEyeSize)

            drawAnchor(canvas, face.anchorPosition, face.baseSize * face.anchorSize)
            //canvas.drawRect(face.boundingBox, eyePaint)
            data.postValue(bmp)
        }
    }

    private fun drawEye(canvas: Canvas, eye: Point, radius: Float) {
        canvas.drawCircle(eye.x.toFloat(), eye.y.toFloat(), radius, eyePaint)
        canvas.drawCircle(eye.x.toFloat(), eye.y.toFloat(), radius / 3, pupilPaint)
    }

    private fun drawAnchor(canvas: Canvas, cheek: Point, size: Float) {
        val drawable = app.resources.getDrawable(R.drawable.ic_anchor)
        drawable.setBounds((cheek.x.toFloat() - size).toInt(), (cheek.y.toFloat() - size).toInt(), (cheek.x.toFloat() + size).toInt(), (cheek.y.toFloat() + size).toInt())
        drawable.draw(canvas)

    }

    private fun getDimensions(uri: Uri): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(app.contentResolver.openInputStream(uri), null, options)
        return Pair(options.outWidth, options.outHeight)
    }

    fun changeSize(factor: Float, featureType: FeatureType, pos: Int = 0) {
        when (featureType) {
            FeatureType.RIGHT_EYE -> knutFaces[pos].rightEyeSize += factor
            FeatureType.LEFT_EYE -> knutFaces[pos].leftEyeSize += factor
            FeatureType.ANCHOR -> knutFaces[pos].anchorSize += factor
        }
        drawFaces()
    }

    fun moveFeature(amount: Int, featureType: FeatureType, direction: Direction, pos: Int = 0) {
        when (featureType) {
            FeatureType.RIGHT_EYE -> {
                when (direction) {
                    Direction.UP -> knutFaces[pos].rightEyePosition.y -= amount
                    Direction.RIGHT -> knutFaces[pos].rightEyePosition.x += amount
                    Direction.DOWN -> knutFaces[pos].rightEyePosition.y += amount
                    Direction.LEFT -> knutFaces[pos].rightEyePosition.x -= amount
                }
            }
            FeatureType.LEFT_EYE -> {
                when (direction) {
                    Direction.UP -> knutFaces[pos].leftEyePosition.y -= amount
                    Direction.RIGHT -> knutFaces[pos].leftEyePosition.x += amount
                    Direction.DOWN -> knutFaces[pos].leftEyePosition.y += amount
                    Direction.LEFT -> knutFaces[pos].leftEyePosition.x -= amount
                }
            }
            FeatureType.ANCHOR -> {
                when (direction) {
                    Direction.UP -> knutFaces[pos].anchorPosition.y -= amount
                    Direction.RIGHT -> knutFaces[pos].anchorPosition.x += amount
                    Direction.DOWN -> knutFaces[pos].anchorPosition.y += amount
                    Direction.LEFT -> knutFaces[pos].anchorPosition.x -= amount
                }
            }
        }
        drawFaces()
    }

}