package com.example.mytestvk.presenter

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytestvk.model.Product
import com.example.mytestvk.view.IUploadedProductsView
import org.json.JSONObject

class GetProducts (val uploadProductsView: IUploadedProductsView, val context: Context): IGetProducts{
    override fun loadProducts(count: Int, skip: Int) {
        val url = "https://dummyjson.com/products?skip=$skip&$count=20"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                    result -> parseProducts(result)

            },
            {
                    error -> uploadProductsView.loadProductsListError("Error")

            }
        )
        queue.add(request)
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