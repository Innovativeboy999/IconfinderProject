package com.example.iconfinder.ui.allIcons

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.api.POST_PER_PAGE
import com.example.iconfinder.data.repository.IconDataSource
import com.example.iconfinder.data.repository.IconDataSourceFactory
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.iconList.Icon
import io.reactivex.disposables.CompositeDisposable

class IconPageListRepository(private val apiService :IconInterface , private var queryString: String) {
    lateinit var iconPagedList :LiveData<PagedList<Icon>>
    lateinit var iconDatasourceFactory:IconDataSourceFactory

    fun fetchLiveIconPagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Icon>>
    {
        iconDatasourceFactory= IconDataSourceFactory(apiService,compositeDisposable, queryString  )
        val config: PagedList.Config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(POST_PER_PAGE)
                .build()

        iconPagedList=LivePagedListBuilder(iconDatasourceFactory,config).build()
        return iconPagedList
    }

    fun getNetworkState():LiveData<NetworkState>
    {
        return Transformations.switchMap<IconDataSource, NetworkState>(
                iconDatasourceFactory.iconlivedataSource,IconDataSource::networkState
        )
    }

    fun getClassDataFactoryReference():IconDataSourceFactory
    {
        return iconDatasourceFactory
    }


}