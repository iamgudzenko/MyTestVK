package com.example.mytestvk.presenter

import android.content.Context
import android.graphics.Bitmap
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytestvk.model.Product
import com.example.mytestvk.view.InfoProductView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class GetInfoProduct(val infoProductView: InfoProductView, val context:Context?): IGetInfoProduct {
    override fun getProductInfo(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            val url = "https://dummyjson.com/products/$id"
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                url,
                {
                        result -> parseInfoProduct(result)

                },
                {
                        error -> infoProductView.errorGetInfoProduct(error.toString())

                }
            )
            queue.add(request)

        }
    }
    fun parseInfoProduct(result: String) {
        val mainObject = JSONObject(result)
        val product = Product(
            mainObject.getInt("id"),
            mainObject.getString("title"),
            mainObject.getString("description"),
            mainObject.getString("thumbnail"),
            mainObject.getString("price"),
            mainObject.getString("brand"))
        val imagesArrayJS = mainObject.getJSONArray("images")
        val imageListUrl = ArrayList<String>()

        for(i in 0 until imagesArrayJS.length()) {
            imageListUrl.add(imagesArrayJS[i].toString())
            product.images.add(getBitmapProduct(imagesArrayJS[i].toString()))

        }

        infoProductView.infoProductViewSuccess(product)
    }

    fun getBitmapProduct(url: String) : Bitmap? {
        var imageBitmap: Bitmap? = null
        CoroutineScope(Dispatchers.IO).launch {
            val imageRequest = ImageRequest(url, {
                imageBitmap = it
            }, 0, 0, null, Bitmap.Config.ARGB_8888, {

            })

            Volley.newRequestQueue(context).add(imageRequest)
        }
        return imageBitmap
    }
}