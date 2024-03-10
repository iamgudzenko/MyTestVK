package com.example.mytestvk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.example.mytestvk.databinding.ItemProductBinding
import com.example.mytestvk.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface ProductActionListener {
    fun goToProductInfo(product: Product)
}
class ProductAdapter(private val context: Context, private val actionListener: ProductActionListener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), View.OnClickListener {

    var products: List<Product?> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        return ProductViewHolder(binding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            holder.itemView.tag = product
            title.text = product?.title
            descr.text = product?.description
            CoroutineScope(Dispatchers.IO).launch {
                val imageRequest = ImageRequest(product!!.thumbnail, {
                    photoImageView.setImageBitmap(it)
                }, 0, 0, null, Bitmap.Config.ARGB_8888, {

                })

                Volley.newRequestQueue(context).add(imageRequest)
            }
        }
    }

    override fun onClick(v: View) {
        val product = v.tag as Product
        actionListener.goToProductInfo(product)
    }
}