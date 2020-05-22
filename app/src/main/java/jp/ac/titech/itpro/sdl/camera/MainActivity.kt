package jp.ac.titech.itpro.sdl.camera

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var photoImage: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photo_button.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // TODO: You should setup appropriate parameters for the intent
        val activity: ComponentName? = intent.resolveActivity(packageManager)
        activity?.let {
            startActivityForResult(intent, REQ_PHOTO)
        } ?: run {
            Toast.makeText(this@MainActivity, R.string.toast_no_activities, Toast.LENGTH_LONG).show()
        }
    }

    private fun showPhoto() {
        photoImage?.let {
            photo_view.setImageBitmap(photoImage)
        }
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)
        if ((reqCode == REQ_PHOTO) && (resCode == Activity.RESULT_OK)) {
            // TODO: You should implement the code that retrieve a bitmap image
            photoImage = data?.let {
                it.extras?.get("data") as Bitmap
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