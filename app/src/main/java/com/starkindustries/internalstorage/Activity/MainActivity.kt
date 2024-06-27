package com.starkindustries.internalstorage.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.internalstorage.R
import com.starkindustries.internalstorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.cameraAndGallery.setOnClickListener()
        {
            var cameraAndGallery = Intent(this, CameraAndGalleryActivity::class.java)
            startActivity(cameraAndGallery)
        }
        binding.pdfActivity.setOnClickListener()
        {
            var pdfActivity = Intent(this, PdfViewerActivity::class.java)
            startActivity(pdfActivity)
        }
        binding.musicPlayer.setOnClickListener()
        {
            var inext = Intent(this,MusicActivity::class.java)
            startActivity(inext)
        }
        binding.videoPlayer.setOnClickListener()
        {
            val intent = Intent(this,VideoPlayerActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}