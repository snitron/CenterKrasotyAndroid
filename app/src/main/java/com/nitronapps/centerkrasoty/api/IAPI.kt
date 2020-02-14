package com.nitronapps.centerkrasoty.api

import com.nitronapps.centerkrasoty.model.AuthResponse
import com.nitronapps.centerkrasoty.model.SMSResponse
import io.reactivex.Observable

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IAPI {
    @FormUrlEncoded
    @POST("registerNewUser")
    fun registerNewUser(@Field("login") login: String,
                        @Field("password") password: String,
                        @Field("phone") phone: String) : Observable<AuthResponse>

    @FormUrlEncoded
    @POST("smsRequest")
    fun smsRequest(@Field("login") login: String,
                   @Field("code") code: Int) : Observable<SMSResponse>

    @FormUrlEncoded
    @POST("loginByPass/")
    fun loginByPassword(@Field("login") login: String,
                        @Field("password") password: String,
                        @Field("phone") phone: String) : Observable<AuthResponse>
}