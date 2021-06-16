package com.example.iconfinder.ui.allIcons

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.iconList.Icon
import io.reactivex.disposables.CompositeDisposable


class MainActivityViewModel(private val iconRepository:IconPageListRepository): ViewModel() {
    private val compositeDisposable =CompositeDisposable()

    val iconPagedList:LiveData<PagedList<Icon>> by lazy { iconRepository.fetchLiveIconPagedList(compositeDisposable) }
    val networkState:LiveData<NetworkState> by lazy { iconRepository.getNetworkState() }

    fun listISEmpty():Boolean{
        return iconPagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}