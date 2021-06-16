package com.example.iconfinder.ui.singleIconDetaias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.iconfinder.data.api.IconClient
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.IconDetails
import com.example.iconfinder.databinding.ActivityIconDetailsBinding

class IconDetailsActivity : AppCompatActivity() {

    private lateinit var globalBinding: ActivityIconDetailsBinding
    private lateinit var viewModel: SingleIconViewModel
    private lateinit var iconDetailsRepository: IconDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalBinding= ActivityIconDetailsBinding.inflate(layoutInflater)
        setContentView(globalBinding.root)

        val iconId : Int =intent.getIntExtra("id",1)

        val apiService : IconInterface = IconClient.getClient()

        iconDetailsRepository= IconDetailsRepository(apiService)
        viewModel=getViewModel(iconId)
        viewModel.iconDetails.observe(this, Observer {

            Log.i("11111", "onCreate: "+it.publishedAt)
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            globalBinding.progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            globalBinding.txtError.visibility=if(it==NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it : IconDetails)
    {
        Log.i("1111111", "bindUI:test loaded or not "+it.isPremium)
//        Log.i("11111", "bindUI: "+it.rasterSizes[5].formats[0].previewUrl)
        val iconUrl : String =it.rasterSizes?.get(6).formats?.get(0).previewUrl
//        Log.i("1111111", "bindUI: "+iconUrl)
        Glide.with(this).load(iconUrl).into(globalBinding.iconPic)
    }






    private fun getViewModel(iconId: Int):SingleIconViewModel
    {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleIconViewModel(iconDetailsRepository,iconId) as T
            }
        }) [SingleIconViewModel::class.java]
    }
}