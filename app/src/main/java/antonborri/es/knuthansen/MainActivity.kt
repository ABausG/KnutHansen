package antonborri.es.knuthansen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                MainFragment.RC_PICK_PHOTO -> {
                    val bundle = Bundle()
                    bundle.putString("photo", data?.data.toString())
                    Navigation.findNavController(this, R.id.nav_host).navigate(R.id.action_process_image, bundle)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(this, R.id.nav_host).navigateUp()

}
