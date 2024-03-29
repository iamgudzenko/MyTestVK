package com.example.mytestvk.presenter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytestvk.model.Product
import com.example.mytestvk.view.IUploadedProductsView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class GetProducts (val uploadProductsView: IUploadedProductsView, val context: Context): IGetProducts{
    override fun loadProducts(page:Int) {
        val skip = 20 * (page - 1)
        CoroutineScope(Dispatchers.IO).launch {
//            delay(20000)

            val url = "https://dummyjson.com/products?skip=$skip&limit=20"
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                url,
                {
                        result -> parseProducts(result)

                },
                {
                        error -> uploadProductsView.loadProductsListError(error.toString())

                }
            )
            queue.add(request)

        }

    }

    fun parseProducts(result: String) {
        val listProducts = ArrayList<Product>()
        val mainObject = JSONObject(result)
        val productsJsonArray = mainObject.getJSONArray("products")
        for(i in 0 until productsJsonArray.length()) {
            val productJS = productsJsonArray[i] as JSONObject
            val product = Product(
                productJS.getInt("id"),
                productJS.getString("title"),
                productJS.getString("description"),
                productJS.getString("thumbnail"))

                listProducts.add(product)
        }
        uploadProductsView.loadProductsListSuccess(listProducts)
    }

}