package com.example.mytestvk.view

import com.example.mytestvk.model.Product

interface IUploadedProductsView {
    fun loadProductsListSuccess(listProducts: ArrayList<Product>)
    fun loadProductsListError(error: String)
}