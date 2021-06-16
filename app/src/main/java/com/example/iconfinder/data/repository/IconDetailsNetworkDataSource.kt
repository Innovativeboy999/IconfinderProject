package com.example.iconfinder.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.iconfinder.data.api.API_KEY
import com.example.iconfinder.data.api.IconClient
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.vo.IconDetails
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import java.lang.Exception

class IconDetailsNetworkDataSource(private val apiSevice:IconInterface , private val compositeDisposable :CompositeDisposable) {
    private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
        get()=_networkState

    private val _downloadedIconDetailsResponse = MutableLiveData<IconDetails>()
    val downloadedIconDetailsResponse: LiveData<IconDetails>
        get()=_downloadedIconDetailsResponse

    fun fetchIconDetails(iconId :Int)
    {
        _networkState.postValue(NetworkState.LOADING)


        try {
            compositeDisposable.add(
                apiSevice.getIconOnIdDetails(API_KEY,iconId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _downloadedIconDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("IconDetails DataSource", "fetchIconDetails: "+it.message )
                        })
            )

        }catch (e:Exception)
        {
            Log.e("IconDetails DataSource", "fetchIconDetails: "+e.message )
        }
    }
}