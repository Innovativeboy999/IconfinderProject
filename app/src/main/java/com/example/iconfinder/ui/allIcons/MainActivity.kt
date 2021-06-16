package com.example.iconfinder.ui.allIcons

import android.Manifest
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.File

class MainActivity : AppCompatActivity(), AllIconsPageListAdapter.PassValueInterface {
    private lateinit var globalBinding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var iconRepository:IconPageListRepository
    private var queryString=""
    var msg:String?=""
    var lastMsg:String?=""
    var downloadUrlReceived:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(globalBinding.root)

        val apiService : IconInterface=IconClient.getClient()
        iconRepository= IconPageListRepository(apiService, queryString)
        viewModel=getViewModel()


        val iconAdapter =AllIconsPageListAdapter(this, this)
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
                    iconRepository.getClassDataFactoryReference().refresh(""+query)

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




    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                        .setTitle("Permission required")
                        .setMessage("Permission required to save photos from the Web.")
                        .setPositiveButton("Accept") { dialog, id ->
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                            finish()
                        }
                        .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                        .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(downloadUrlReceived)
        }
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    }

    fun downloadImage( downloadUrl:String?)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        } else {
            val directory = File(Environment.DIRECTORY_PICTURES)

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val downloadUri = Uri.parse(downloadUrl)

            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(downloadUrl?.substring(downloadUrl.lastIndexOf("/") + 1))
                        .setDescription("")
                        .setDestinationInExternalPublicDir(
                                directory.toString(),
                                downloadUrl?.substring(downloadUrl.lastIndexOf("/") + 1)
                        )
            }

            val downloadId = downloadManager.enqueue(request)
            val query = DownloadManager.Query().setFilterById(downloadId)
            Thread(Runnable {
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                    }
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    msg = statusMessage(downloadUrl, directory, status)
                    if (msg != lastMsg) {
                        this.runOnUiThread {
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                        lastMsg = msg ?: ""
                    }
                    cursor.close()
                }
            }).start()
        }
    }

    private fun statusMessage(url: String?, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url?.substring(
                    url?.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage(downloadUrlReceived)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun passResultCallback(message: String) {
        downloadUrlReceived=message
        downloadImage(downloadUrlReceived)
    }
}