package com.example.mp3service

import android.os.Binder

class Mp3Binder(val service: Mp3Service) : Binder()