package com.example.iconfinder.ui.allIcons

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.iconfinder.R
import com.example.iconfinder.data.repository.NetworkState
import com.example.iconfinder.data.vo.iconList.Icon
import com.example.iconfinder.databinding.IconListItemBinding
import com.example.iconfinder.databinding.NetworkStateItemBinding
import com.example.iconfinder.ui.singleIconDetaias.IconDetailsActivity
import java.io.File


class AllIconsPageListAdapter(public val context: Context, val referenceInterface:PassValueInterface) :PagedListAdapter<Icon, RecyclerView.ViewHolder>(IconDiffCallback())
{
    val ICON_VIEW_TYPE=1
    val NETWORK_VIEW_TYPE=2
    private var networkState:NetworkState?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == ICON_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.icon_list_item, parent, false)
            return IconItemViewHolder(view, referenceInterface)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ICON_VIEW_TYPE) {
            (holder as IconItemViewHolder).bind(getItem(position), context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            ICON_VIEW_TYPE
        }
    }

    class IconDiffCallback : DiffUtil.ItemCallback<Icon>(){
        override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem.iconId==newItem.iconId
        }

        override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem==newItem
        }

    }

    class IconItemViewHolder(view: View,val  referenceInterface: PassValueInterface) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private lateinit var globalIconBinding:IconListItemBinding
        lateinit var downloadUrl:String
        fun bind(icon: Icon?, context: Context)
        {
            globalIconBinding= IconListItemBinding.bind(itemView.rootView)
            globalIconBinding.cvIconTitle.text=icon?.tags?.get(0)
            if(icon?.isPremium==true)
            {
                globalIconBinding.cvIconReleaseDate.text="Rs." + icon?.prices?.get(0).price.toString()
                globalIconBinding.cvIvPaidPosterLeft.visibility=View.VISIBLE
            }
            else{
                globalIconBinding.cvIconReleaseDate.visibility=View.GONE
                globalIconBinding.cvIconDownloadBtn.visibility=View.VISIBLE
                globalIconBinding.cvIconDownloadBtn.setOnClickListener(this)
                downloadUrl=""+icon?.rasterSizes?.get(6)?.formats?.get(0)?.downloadUrl
            }

            Glide.with(itemView.context).load(icon?.rasterSizes?.get(6)?.formats?.get(0)?.previewUrl).into(globalIconBinding.cvIvIconPoster)

            itemView.setOnClickListener {
                val intent = Intent(context, IconDetailsActivity::class.java)
                intent.putExtra("id", icon?.iconId)
                context.startActivity(intent)
            }
        }

        override fun onClick(v: View?) {
            when(v?.id)
            {
                R.id.cv_icon_download_btn -> {
                    referenceInterface.passResultCallback(downloadUrl)

                }
            }
        }


    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var globalNetworkBinding:NetworkStateItemBinding
        fun bind(networkState: NetworkState?) {
            globalNetworkBinding= NetworkStateItemBinding.bind(itemView.rootView)


            if (networkState != null && networkState == NetworkState.LOADING) {

                globalNetworkBinding.progressBarItem.visibility = View.VISIBLE;
            }
            else  {
                globalNetworkBinding.progressBarItem.visibility = View.GONE;
            }


            if (networkState != null && networkState == NetworkState.ERROR) {
                globalNetworkBinding.errorMsgItem.visibility = View.VISIBLE;
                globalNetworkBinding.errorMsgItem.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                globalNetworkBinding.errorMsgItem.visibility = View.VISIBLE;
                globalNetworkBinding.errorMsgItem.text = networkState.msg;
            }
            else {
                globalNetworkBinding.errorMsgItem.visibility = View.GONE;
            }
        }


    }





    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }
    }

    interface PassValueInterface {
        fun passResultCallback(message: String)
    }
}