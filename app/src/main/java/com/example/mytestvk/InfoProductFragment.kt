package com.example.mytestvk

import android.R.attr.key
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytestvk.databinding.FragmentInfoProductBinding
import com.example.mytestvk.model.Product
import com.example.mytestvk.presenter.GetInfoProduct
import com.example.mytestvk.presenter.IGetInfoProduct
import com.example.mytestvk.view.InfoProductView
import com.google.android.material.carousel.CarouselLayoutManager


class InfoProductFragment : Fragment(), InfoProductView {
    lateinit var binding: FragmentInfoProductBinding
    private lateinit var getInfoProduct: IGetInfoProduct
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoProductBinding.inflate(layoutInflater)
        getInfoProduct = GetInfoProduct(this, context)

        val id = arguments?.getInt("id")
        getInfoProduct.getProductInfo(id!!)


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) :InfoProductFragment {
            val fragment = InfoProductFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun infoProductViewSuccess(product: Product) {
        binding.titleInfoProduct.text = product.title
        binding.descriptionInfoProduct.text = product.description
        binding.brandInfoProduct.text = product.brand
        binding.priceInfoProduct.text = product.price
        binding.imageProductInfo.setImageBitmap(product.images[0])
    }

    override fun loadImageProductSuccess() {


    }

    override fun errorGetInfoProduct(error: String) {

    }
}