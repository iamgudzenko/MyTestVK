package com.example.mytestvk

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytestvk.databinding.ActivityMainBinding
import com.example.mytestvk.model.Product
import com.example.mytestvk.presenter.GetProducts
import com.example.mytestvk.presenter.IGetProducts
import com.example.mytestvk.view.IUploadedProductsView


class MainActivity : AppCompatActivity(), IUploadedProductsView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var getProducts: IGetProducts
    private lateinit var adapter: ProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts = GetProducts(this, applicationContext)

        adapter = ProductAdapter(applicationContext, object : ProductActionListener{
            override fun goToProductInfo(product: Product) {

            }

        })
        getProducts.loadProducts(20,0)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadProductsListSuccess(listProducts: ArrayList<Product>) {
        adapter.products = listProducts
        binding.recyclerViewProducts.setLayoutManager(GridLayoutManager(this, 2))
        binding.recyclerViewProducts.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun loadProductsListError(error: String) {
        Log.d("isResult", "ERROR")
    }
}