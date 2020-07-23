package com.example.catapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*


class CatsRecyclerAdapter(
    val context: Context,
    val listener: CatClickListener
) : RecyclerView.Adapter<ViewHolder>() {
    val catsList = mutableListOf<CatDataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, null)
        return ViewHolder(view)
    }

    override fun getItemCount() = catsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = catsList[position].url
        holder.bind(imageUrl)

        holder.imageView.setOnLongClickListener {
            saveImage(getBitmapFromView(it as ImageView), catsList[position].id)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            true
        }

        holder.imageView.setOnClickListener {
            listener.onCatClick(holder.imageView.drawable.toBitmap())
        }
    }

    fun addAll(items: List<CatDataItem>) {
        catsList.addAll(items)
        notifyDataSetChanged()
    }

    private fun saveImage(bitmap: Bitmap, title: String) {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
    }

    private fun getBitmapFromView(view: ImageView): Bitmap {
        val bitmap: Bitmap = (view.drawable as BitmapDrawable).bitmap
        return bitmap
    }


}