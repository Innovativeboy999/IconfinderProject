package com.example.iconfinder.ui.allIcons

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iconfinder.R
import com.example.iconfinder.data.api.IconClient
import com.example.iconfinder.data.api.IconInterface
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.databinding.ActivityMainBinding
import com.example.iconfinder.ui.singleIconDetaias.IconDetailsActivity
import com.example.iconfinder.ui.singleIconDetaias.SingleIconViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var globalBinding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var iconRepository:IconPageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(globalBinding.root)

        val apiService : IconInterface=IconClient.getClient()

        iconRepository= IconPageListRepository(apiService)

        viewModel=getViewModel()

        val iconAdapter =AllIconsPageListAdapter(this)
        val gridLayoutManager=GridLayoutManager(this, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = iconAdapter.getItemViewType(position)
                if (viewType == iconAdapter.ICON_VIEW_TYPE) return  1    // Movie_VIEW_TYPE will occupy 1 out of 2 span
                else return 2                                              // NETWORK_VIEW_TYPE will occupy all 2 span
            }
        };

        globalBinding.rvMainActivity.layoutManager=gridLayoutManager
        globalBinding.rvMainActivity.setHasFixedSize(true)
        globalBinding.rvMainActivity.adapter=iconAdapter

        viewModel.iconPagedList.observe(this, Observer {
            iconAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {


            globalBinding.progressBarMain.visibility = if (viewModel.listISEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            globalBinding.txtErrorMain.visibility = if (viewModel.listISEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listISEmpty()) {
                iconAdapter.setNetworkState(it)
            }
        })

    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchview, menu)

        val searchItem = menu.findItem(R.id.searchIcon)
        if(searchItem!=null)
        {
            val searchView=searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(applicationContext, ""+query, Toast.LENGTH_SHORT).show()
                    searchView.clearFocus()

                    return true
                }
            })
        }
        
        return super.onCreateOptionsMenu(menu)
    }
    private fun getViewModel(): MainActivityViewModel {

        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel( iconRepository) as T
            }
        }) [MainActivityViewModel::class.java]
    }

}