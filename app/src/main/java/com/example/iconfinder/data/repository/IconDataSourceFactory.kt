package com.example.iconfinder.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.vo.iconList.Icon
import io.reactivex.disposables.CompositeDisposable


class IconDataSourceFactory(private val apiService: IconInterface, private val compositeDisposable: CompositeDisposable , private var queryString :String) : DataSource.Factory<Int, Icon>()
{
    val iconlivedataSource = MutableLiveData<IconDataSource>()
    override fun create(): DataSource<Int, Icon> {
        val icondataSource =IconDataSource(apiService,compositeDisposable ,queryString )
        iconlivedataSource.postValue(icondataSource)
        return icondataSource
    }

    fun refresh(queryString: String)
    {
        this.queryString=queryString
        iconlivedataSource.value?.invalidate()

    }

}