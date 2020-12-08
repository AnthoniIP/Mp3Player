package com.example.mp3service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import java.io.FileInputStream

class Mp3ServiceImp : Service(),Mp3Service {
    private lateinit var mediaPlayer : MediaPlayer
    private var isPaused : Boolean = false
    private var currentFile: String? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Mp3Binder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            when(intent.getStringExtra(EXTRA_ACTION)) {
                ACTION_PLAY -> play(intent.getStringExtra(EXTRA_FILE)!!)
                ACTION_PAUSE -> pause()
                ACTION_STOP -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun play(file: String) {
        if(!mediaPlayer.isPlaying && !isPaused) {
            try {
                mediaPlayer.reset()
                val fis = FileInputStream(file)
                mediaPlayer.setDataSource(fis.fd)
                mediaPlayer.prepare()
                currentFile = file
            }catch(e:Exception) {
                e.printStackTrace()
                return
            }
        }
        isPaused = false
        mediaPlayer.start()
    }
    override fun pause() {
        if(mediaPlayer.isPlaying) {
            isPaused = true
            mediaPlayer.pause()
        }
    }

    override fun stop() {
        if(mediaPlayer.isPlaying || isPaused) {
            isPaused = false
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    override var currentSong: String? = null
        get() = currentFile

    override val totalTime: Int
        get() {
            return if(mediaPlayer.isPlaying || isPaused) {
                mediaPlayer.duration
            }else 0
        }

    override val elapsedTime: Int
        get() {
            return if(mediaPlayer.isPlaying || isPaused) {
                mediaPlayer.currentPosition
            }else 0
        }

    companion object {
        val EXTRA_ACTION = "${Mp3ServiceImp::class.java.`package`.name}.EXTRA_ACTION"
        val EXTRA_FILE = "${Mp3ServiceImp::class.java.`package`.name}.EXTRA_FILE"
        val ACTION_PLAY = "${Mp3ServiceImp::class.java.`package`.name}.ACTION_PLAY"
        val ACTION_PAUSE = "${Mp3ServiceImp::class.java.`package`.name}.ACTION_PAUSE"
        val ACTION_STOP = "${Mp3ServiceImp::class.java.`package`.name}.ACTION_STOP"
    }

}