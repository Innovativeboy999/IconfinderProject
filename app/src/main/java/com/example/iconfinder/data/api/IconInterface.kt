package com.example.iconfinder.data.api

import com.example.iconfinder.data.vo.IconDetails
import com.example.iconfinder.data.vo.iconList.ListIcons
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.iconfinder.com/v4/icons/search?count=300
//https://api.iconfinder.com/v4/icons/182504
//https://api.iconfinder.com/v4/
interface IconInterface {

    @GET("icons/{icon_id}")
    fun getIconOnIdDetails(@Header("Authorization") token: String ,
        @Path("icon_id") id :Int) : Single<IconDetails>


    @GET("icons/search")
    fun getListIcons(@Header("Authorization") token: String ,
    @Query("count") numberLimit : Int ,
    @Query("offset") offset : Int) : Single<ListIcons>
}