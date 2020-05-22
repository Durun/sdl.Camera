package jp.ac.titech.itpro.sdl.camera

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private var photoImage: Bitmap? = null
    private val photoFile: File by lazy {
        File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/$PHOTO_FILE_PREFIX.jpg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadPhoto()

        photo_button.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // TODO: You should setup appropriate parameters for the intent
        val photoUri = FileProvider.getUriForFile(this, FILEPROVIDER_AUTHORITY, photoFile)
        val activity: ComponentName? = intent.resolveActivity(packageManager)

        activity?.let {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQ_PHOTO)
        } ?: run {
            Toast.makeText(this@MainActivity, R.string.toast_no_activities, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadPhoto() {
        photoImage = photoFile.decodeForView(photo_view)
    }

    private fun showPhoto() {
        photoImage?.let { photo_view.setImageBitmap(it) }
    }

    override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)
        if ((reqCode == REQ_PHOTO) && (resCode == Activity.RESULT_OK)) {
            // TODO: You should implement the code that retrieve a bitmap image
            loadPhoto()
        }
    }

    override fun onResume() {
        super.onResume()
        showPhoto()
    }

    private fun File.decodeForView(view: ImageView): Bitmap? {
        return this.takeIf { this.exists() }?.let {
            BitmapFactory.decodeFile(it.path)
        }
    }

    companion object {
        private const val REQ_PHOTO = 1234
        private const val PHOTO_FILE_PREFIX = "sample-photo"
        private const val FILEPROVIDER_AUTHORITY = "jp.ac.titech.itpro.sdl.camera.fileprovider"
    }
}