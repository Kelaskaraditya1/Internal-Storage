package com.starkindustries.internalstorage.Activity
import android.content.Intent
import android.media.browse.MediaBrowser.MediaItem
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.exoplayer.ExoPlayer
import com.starkindustries.internalstorage.R
import com.starkindustries.internalstorage.databinding.ActivityVideoPlayerBinding
class VideoPlayerActivity : AppCompatActivity() {
    lateinit var binding:ActivityVideoPlayerBinding
    lateinit var player:ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_player)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_video_player)
        binding.fileManager.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("video/*")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent,101)
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
            if(requestCode==101)
            {
                player=ExoPlayer.Builder(this).build()
                binding.exoPlayer.player=player
                var mediaItem =androidx.media3.common.MediaItem.fromUri(data?.data!!)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }
        }
    }
}