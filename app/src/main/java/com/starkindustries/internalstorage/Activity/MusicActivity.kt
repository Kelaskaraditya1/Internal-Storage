package com.starkindustries.internalstorage.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.internalstorage.R
import com.starkindustries.internalstorage.databinding.ActivityMusicBinding
import java.util.Timer
import kotlin.concurrent.timerTask


class MusicActivity : AppCompatActivity() {
    lateinit var binding:ActivityMusicBinding
    lateinit var player:MediaPlayer
    lateinit var manager:AudioManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_music)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_music)
        player= MediaPlayer()
        manager = getSystemService(AUDIO_SERVICE) as AudioManager
        var maxVol = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        var curVol = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.volumeSeekbar.max=maxVol
        binding.volumeSeekbar.progress=curVol
        binding.volumeSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let { manager.setStreamVolume(AudioManager.STREAM_MUSIC, it,0) }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?)
            {
                seekBar?.progress?.let { manager.setStreamVolume(AudioManager.STREAM_MUSIC, it,0) }
            }
        })
        binding.fileManager.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("audio/pdf/docs/*") // Set the MIME type to audio files
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Select Music"),102)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!!player.isPlaying&&player!=null)
            player.pause()
        player.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            if(requestCode==102)
            {
                player=MediaPlayer.create(applicationContext,data?.data)
                binding.musicSeekbar.max=player.duration
                val handler = Handler()
                handler.postDelayed(object:Runnable{
                    override fun run() {
                        binding.musicSeekbar.setProgress(player.currentPosition)
                        handler.postDelayed(this,1000)
                    }

                },0)
                binding.musicSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if(fromUser)
                            player.seekTo(progress)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                       seekBar?.progress.let { player.seekTo(it!!) }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        seekBar?.progress.let { player.seekTo(it!!) }
                    }
                })
                binding.play.setOnClickListener()
                {
                    if(!player.isPlaying&&player!=null)
                        player.start()
                }
                binding.pause.setOnClickListener()
                {
                    if(player!=null&&player.isPlaying)
                        player.pause()
                }
                binding.stop.setOnClickListener()
                {
                    if(player.isPlaying&&player!=null)
                        player.stop()
                }
            }
        }
    }
}