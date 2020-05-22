package jp.ac.titech.itpro.sdl.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.ac.titech.itpro.sdl.camera.MainActivity

class MainActivity : AppCompatActivity() {
    private val photoImage: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val photoButton = findViewById<Button>(R.id.photo_button)
        photoButton.setOnClickListener {
            val intent = Intent()
            // TODO: You should setup appropriate parameters for the intent
            val manager = packageManager
            val activities: List<*> = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (!activities.isEmpty()) {
                startActivityForResult(intent, REQ_PHOTO)
            } else {
                Toast.makeText(this@MainActivity, R.string.toast_no_activities, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPhoto() {
        if (photoImage == null) {
            return
        }
        val photoView = findViewById<ImageView>(R.id.photo_view)
        photoView.setImageBitmap(photoImage)
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)
        if (reqCode == REQ_PHOTO) {
            if (resCode == Activity.RESULT_OK) {
                // TODO: You should implement the code that retrieve a bitmap image
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showPhoto()
    }

    companion object {
        private const val REQ_PHOTO = 1234
    }
}