package com.example.iconfinder.ui.singleIconDetaias

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.IconDetails
import io.reactivex.disposables.CompositeDisposable

class SingleIconViewModel (private val iconDetailsRepository: IconDetailsRepository , iconId : Int) :ViewModel()
{
    private val compositeDisposable= CompositeDisposable()
    val iconDetails : LiveData<IconDetails>  by lazy{
        iconDetailsRepository.fetchSingleIconDetails(compositeDisposable,iconId)}

    val networkState :LiveData<NetworkState> by lazy {
        iconDetailsRepository.getIconDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}