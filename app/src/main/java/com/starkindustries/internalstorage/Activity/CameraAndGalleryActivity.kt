package com.starkindustries.internalstorage.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.internalstorage.R
import com.starkindustries.internalstorage.databinding.ActivityCameraAndGalleryBinding
import java.io.File
import java.io.FileOutputStream

class CameraAndGalleryActivity : AppCompatActivity() {
    lateinit var binding:ActivityCameraAndGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera_and_gallery)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_camera_and_gallery)
        binding.camera.setOnClickListener()
        {
            if(ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {
                var camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(camera,100)
            }
            else
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA),1)
        }
        binding.gallery.setOnClickListener()
        {
            var gallery = Intent(Intent.ACTION_PICK)
            gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,101)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            if(requestCode==100) {
                var bitmap: Bitmap = data?.extras?.get("data") as Bitmap
                lateinit var filelOutStream: FileOutputStream
                var file: File = File(externalCacheDir, "image.Jpeg")
                try {
                    filelOutStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, filelOutStream)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                binding.imageView.setImageURI(Uri.fromFile(file))
            }
            if(requestCode==101)
            {
                binding.imageView.setImageURI(data?.data!!)
                data?.data?.let {
                        uri->
                    Toast.makeText(applicationContext, "filename: "+getFileName(data.data!!), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    result = it.getString(nameIndex)
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }


}