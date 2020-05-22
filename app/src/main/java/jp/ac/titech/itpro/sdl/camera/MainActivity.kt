package jp.ac.titech.itpro.sdl.camera

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var photoImage: Bitmap? = null
    private val photoFile: File by lazy {
        File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/$PHOTO_FILE_PREFIX.jpg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photo_button.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // TODO: You should setup appropriate parameters for the intent
        val photoUri = createPhotoUri()
        val activity: ComponentName? = intent.resolveActivity(packageManager)
        activity?.let {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
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
            photoImage = photoFile.decodeForView(photo_view)
        }
    }

    override fun onResume() {
        super.onResume()
        showPhoto()
    }

    override fun onDestroy() {
        super.onDestroy()

        photoFile.takeIf { it.exists() }?.delete()  // delete photo file on exit
    }

    private fun File.decodeForView(view: ImageView): Bitmap {
        val option = BitmapFactory.Options().apply {
            val targetW = view.width
            val targetH = view.height
            val photoW = outWidth
            val photoH = outHeight

            val scaleFactor: Int = (photoW / targetW).coerceAtMost(photoH / targetH)
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        return BitmapFactory.decodeFile(this.path, option)
    }

    @Throws(IOException::class)
    private fun createPhotoUri(): Uri {
        photoFile.takeUnless { it.exists() }?.createNewFile()
        return FileProvider.getUriForFile(this, FILEPROVIDER_AUTHORITY, photoFile)
    }

    companion object {
        private const val REQ_PHOTO = 1234
        private const val PHOTO_FILE_PREFIX = "sample-photo"
        private const val FILEPROVIDER_AUTHORITY = "jp.ac.titech.itpro.sdl.camera.fileprovider"
    }
}