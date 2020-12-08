package com.example.mp3service

interface Mp3Service {
    fun play(file : String)
    fun stop()
    fun pause()
    var currentSong : String?
    val totalTime: Int
    val elapsedTime: Int
}