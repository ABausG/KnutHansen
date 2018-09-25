package antonborri.es.knuthansen


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.feature_control.*
import kotlinx.android.synthetic.main.fragment_process.*
import kotlinx.android.synthetic.main.move_controls.*
import kotlinx.android.synthetic.main.zoom_controls.*
import javax.inject.Inject


class ProcessFragment : Fragment() {


    @Inject
    lateinit var faceDetector: FirebaseVisionFaceDetector
    
    @Inject
    lateinit var viewModelFactory: ProcessViewModelFactory

    lateinit var viewModel: ProcessViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_process, container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProcessViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri = Uri.parse(arguments?.getString("photo"))


        if (context != null) {
            val fvi = FirebaseVisionImage.fromFilePath(context!!, uri)
            val result = faceDetector.detectInImage(fvi)
            result.addOnSuccessListener {
                viewModel.setFaces(uri, it)
            }.addOnFailureListener {
                Log.e("FACE FAILURE", it.message)
            }
        }

        viewModel.image.observe(this, Observer {
            img_photo.setImageBitmap(it)
        })

        img_zoom_plus.setOnClickListener { viewModel.zoomFeature(1) }
        img_zoom_minus.setOnClickListener { viewModel.zoomFeature(-1) }

        img_arrow_up.setOnClickListener { viewModel.moveFeature(Direction.UP) }
        img_arrow_right.setOnClickListener { viewModel.moveFeature(Direction.RIGHT) }
        img_arrow_down.setOnClickListener { viewModel.moveFeature(Direction.DOWN) }
        img_arrow_left.setOnClickListener { viewModel.moveFeature(Direction.LEFT) }

        eye_right.setOnClickListener { viewModel.setFeature(FeatureType.RIGHT_EYE) }
        eye_left.setOnClickListener { viewModel.setFeature(FeatureType.LEFT_EYE) }
        anchor.setOnClickListener { viewModel.setFeature(FeatureType.ANCHOR) }

        viewModel.currentFeature.observe(this, Observer {
            selector_right.setVisibility(it == FeatureType.RIGHT_EYE)
            selector_left.setVisibility(it == FeatureType.LEFT_EYE)
            selector_anchor.setVisibility(it == FeatureType.ANCHOR)
        })
    }

}
