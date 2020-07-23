package com.example.catapp

import android.graphics.Bitmap
import android.view.View

interface CatClickListener {
    fun onCatClick(bitmap: Bitmap)
}