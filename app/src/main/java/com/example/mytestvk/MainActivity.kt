package com.example.mytestvk

import android.R.attr.fragment
import android.R.attr.key
import android.R.attr.value
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.infoPageTextView.text = "$page из 5"
        binding.scrollViewProducts.visibility = View.GONE

        getProducts = GetProducts(this, applicationContext)

        adapter = ProductAdapter(applicationContext, object : ProductActionListener{
            override fun goToProductInfo(product: Product) {
                Log.d("isClick2", product.id.toString())
                if(savedInstanceState == null) {
                    val myFragment = InfoProductFragment.newInstance(product.id)
                    supportFragmentManager.beginTransaction().replace(R.id.infoProductFragm, myFragment).commit()
                }

            }

        })
        if(isOnline(applicationContext)) {
            getProducts.loadProducts(page)
        }


        binding.backPageButton.setOnClickListener {
            if(isOnline(applicationContext)) {
                binding.progressLoad.visibility = View.VISIBLE
                binding.scrollViewProducts.visibility = View.GONE
                page -= 1
                binding.infoPageTextView.text = "$page из 5"
                getProducts.loadProducts(page)
            }
        }
        binding.nextPageButton.setOnClickListener {
            if(isOnline(applicationContext)) {
                binding.progressLoad.visibility = View.VISIBLE
                binding.scrollViewProducts.visibility = View.GONE
                page += 1
                binding.infoPageTextView.text = "$page из 5"
                getProducts.loadProducts(page)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun loadProductsListSuccess(listProducts: ArrayList<Product>) {
        binding.progressLoad.visibility = View.GONE
        binding.scrollViewProducts.visibility = View.VISIBLE
        adapter.products = listProducts
        binding.recyclerViewProducts.setLayoutManager(GridLayoutManager(this, 2))
        binding.recyclerViewProducts.adapter = adapter
        adapter.notifyDataSetChanged()
        if(page == 1) {
            binding.backPageButton.visibility = View.GONE
        } else {
            binding.backPageButton.visibility = View.VISIBLE

        }

        if(page == 5) {
            binding.nextPageButton.visibility = View.GONE
        } else {
            binding.nextPageButton.visibility = View.VISIBLE
        }
    }

    override fun loadProductsListError(error: String) {
        Log.d("isResult", error)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", page)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        page = savedInstanceState.getInt("page")
        binding.infoPageTextView.text = "$page из 5"
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show()
        return false
    }
}