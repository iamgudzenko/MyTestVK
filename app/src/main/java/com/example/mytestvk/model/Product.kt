package com.example.mytestvk.model

import android.graphics.Bitmap

data class Product (val id: Int,
                    val title: String,
                    val description: String,
                    val thumbnail: String,
                    var price: String? = null,
                    var brand: String? = null,
                    //для карусели может быть
                    var images: MutableList<Bitmap?> = mutableListOf()
){
}