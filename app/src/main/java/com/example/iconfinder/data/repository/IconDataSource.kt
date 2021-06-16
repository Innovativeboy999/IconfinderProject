package com.example.iconfinder.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.iconfinder.data.api.API_KEY
import com.example.iconfinder.data.api.FIRST_COUNT
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.vo.iconList.Icon
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//Comment for Aakash Sir ,
// This can also be done using PagingSource Library but i have chosen to do it with
// PageKeyedDataSource because i have seen more examples of this and is more confident to implement with this. If i had more time then i would
//surely have gone for PagingSource


class IconDataSource(private val apiService:IconInterface, private val compositeDisposable: CompositeDisposable , private var queryString : String ): PageKeyedDataSource<Int, Icon>()
{
    private var count= FIRST_COUNT
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Icon>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
                apiService.getListIcons(API_KEY, 20, queryString,count)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            callback.onResult(it.icons, null, count + 20)
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("111111", "loadInitial: inside icon data source  load initial"+it.message )
                        })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Icon>)
    {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Icon>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
                apiService.getListIcons(API_KEY, 20, queryString,params.key)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                                   if (it.totalCount>=params.key+20)
                                   {
                                       callback.onResult(it.icons,params.key+20)
                                       networkState.postValue(NetworkState.LOADED)
                                   }
                            else{
                                networkState.postValue(NetworkState.ENDOFLIST)
                                   }
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("111111", "loadInitial: inside icon data source  load After"+it.message )
                        })
        )
    }

    fun api_call(query:String)
    {
        compositeDisposable.add(
        apiService.getListIcons(API_KEY, 20, queryString, 0)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    networkState.postValue(NetworkState.LOADED)
                },{networkState.postValue(NetworkState.ERROR)
                    Log.e("111111", "loadInitial: inside icon data source  load initial"+it.message )

                })
        )
    }

}