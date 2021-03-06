package antonborri.es.knuthansen.injection

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import antonborri.es.knuthansen.ImageDrawer
import antonborri.es.knuthansen.ProcessViewModelFactory
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseOptions(): FirebaseVisionFaceDetectorOptions {
        return FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(true)
                .build()
    }

    @Provides
    fun provideImageDrawer(app: Application) : ImageDrawer = ImageDrawer(app)

    @Provides
    @Singleton
    fun provideFaceDetector(options: FirebaseVisionFaceDetectorOptions): FirebaseVisionFaceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options)

    @Provides
    fun provideProcessViewModel(factory: ProcessViewModelFactory) : ViewModelProvider.Factory = factory
}