package com.example.iconfinder.ui.singleIconDetaias

import androidx.lifecycle.LiveData
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.repository.IconDetailsNetworkDataSource
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.IconDetails
import io.reactivex.disposables.CompositeDisposable

class IconDetailsRepository(private val apiService: IconInterface) {
    lateinit var iconDetailsNetworkDataSource: IconDetailsNetworkDataSource

    fun fetchSingleIconDetails(compositeDisposable: CompositeDisposable, iconId : Int) : LiveData<IconDetails>
    {
        iconDetailsNetworkDataSource= IconDetailsNetworkDataSource(apiService,compositeDisposable)
        iconDetailsNetworkDataSource.fetchIconDetails(iconId)
        return iconDetailsNetworkDataSource.downloadedIconDetailsResponse
    }

    fun getIconDetailsNetworkState() : LiveData<NetworkState>
    {
        return iconDetailsNetworkDataSource.networkState
    }
}