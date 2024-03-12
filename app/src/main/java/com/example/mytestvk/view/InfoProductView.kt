package com.example.mytestvk.view

import com.example.mytestvk.model.Product

interface InfoProductView {
    fun infoProductViewSuccess(product: Product)
    fun loadImageProductSuccess()
    fun errorGetInfoProduct(error:String)
}