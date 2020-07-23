package com.example.catapp

import android.view.View
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import coil.api.load

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val imageView = view.findViewById<ImageView>(R.id.item_imageview)

    fun bind(imageUrl: String) {
        imageView.load(imageUrl)
    }
}