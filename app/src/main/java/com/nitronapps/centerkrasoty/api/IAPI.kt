package com.nitronapps.centerkrasoty.api

import com.nitronapps.centerkrasoty.model.AuthRequest
import com.nitronapps.centerkrasoty.model.AuthResponse
import com.nitronapps.centerkrasoty.model.SMSResponse
import io.reactivex.Observable
import retrofit2.http.*

interface IAPI {

    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    @FormUrlEncoded
    @POST("registerNewUser/")
    fun registerNewUser(@Field("login") login: String,
                        @Field("password") password: String,
                        @Field("name") name: String,
                        @Field("surname") surname: String,
                        @Field("phone") phone: String) : Observable<AuthResponse>

    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    @FormUrlEncoded
    @POST("smsRequest/")
    fun smsRequest(@Field("login") login: String,
                   @Field("code") code: Int) : Observable<SMSResponse>

    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    @FormUrlEncoded
    @POST("loginByPass/")
    fun loginByPassword(@Field("login") login: String,
                        @Field("password") password: String,
                        @Field("phone") phone: String) : Observable<AuthResponse>
}