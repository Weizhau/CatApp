package com.example.catapp

import android.Manifest
import android.animation.Animator
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
* Saving images - long click on image
* */

class MainActivity() : AppCompatActivity(), CoroutineScope, CatClickListener {
    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    private val catsAdapter = CatsRecyclerAdapter(this, this)
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)

        nestedScrollView = findViewById(R.id.scroll_view)
        progressBar = findViewById(R.id.progress_bar)

        recyclerView = findViewById(R.id.cats_recyclerview)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = catsAdapter

            loadCats()
        }

        nestedScrollView.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v != null) {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        progressBar.visibility = View.VISIBLE
                        loadCats()
                    }
                }
            }
        })
    }

    private fun loadCats() {
        val cats = ArrayList<CatDataItem>()

        this.launch {
            cats.addAll(CatsApiImpl.getListOfCats())
            if (!cats.isEmpty()) {
                catsAdapter.addAll(cats)
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun openImageWithRotation(bitmap: Bitmap) {
        val enlargedImageView = findViewById<ImageView>(R.id.image_enlarged)
        enlargedImageView.setImageBitmap(bitmap)

        val copyEnlarged = ImageView(this)
        copyEnlarged.setImageBitmap(bitmap)

        enlargedImageView.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE

        enlargedImageView.cameraDistance = 16000F
        enlargedImageView.animate().withLayer()
            .rotationY(90F)
            .setDuration(300)
            .withEndAction {
                enlargedImageView.setRotationY(-90F);
                enlargedImageView.animate().withLayer()
                    .rotationY(0F)
                    .setDuration(300)
                    .start()
            }
            .start()

        enlargedImageView.setOnClickListener {
            enlargedImageView.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCatClick(bitmap: Bitmap) {
        openImageWithRotation(bitmap)
    }
}
