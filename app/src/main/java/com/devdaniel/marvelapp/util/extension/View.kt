package com.devdaniel.marvelapp.util.extension

import android.view.View
import android.widget.ImageView
import coil.load
import com.devdaniel.marvelapp.R

fun ImageView.loadImageFromUrl(url: String) {
    this.load(url) {
        crossfade(600)
        error(R.drawable.ic_broken_image)
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun isOddOrEven(position: Int): Boolean = position % 2 == 0
