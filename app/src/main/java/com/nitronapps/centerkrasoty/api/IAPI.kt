package com.nitronapps.centerkrasoty.api

import com.nitronapps.centerkrasoty.model.*
import io.reactivex.Observable
import retrofit2.http.*

interface IAPI {

    @FormUrlEncoded
    @POST("addTransaction/")
    @Headers(
        "User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector",
        "Connection: close"
    )
    fun addTransaction(
        @Field("token") token: String,
        @Field("orders") orders: String
    ): Observable<TransactionResponse>

    @FormUrlEncoded
    @POST("deleteOrder/")
    @Headers(
        "User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector",
        "Connection: close"
    )
    fun deleteOrder(
        @Field("token") token: String,
        @Field("orderId") orderId: Int
    ): Observable<DeleteOrderResponse>

    @FormUrlEncoded
    @POST("getOffices/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun getOffices(@Field("token") token: String): Observable<OfficeResponse>

    @FormUrlEncoded
    @POST("getOrders/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun getOrders(
        @Field("token") token: String,
        @Field("groupId") groupServiceId: Int? = null,
        @Field("date") date: String? = null,
        @Field("forUser") forUser: Boolean
    ): Observable<OrderResponse>

    @FormUrlEncoded
    @POST("getPlaces/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun getPlaces(
        @Field("token") token: String,
        @Field("groupId") serviceId: Int
    ): Observable<PlaceResponse>

    @FormUrlEncoded
    @POST("getServices/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun getServices(
        @Field("token") token: String,
        @Field("officeId") officeId: Int
    ): Observable<ServiceResponse>

    @FormUrlEncoded
    @POST("getUser/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun getUser(@Field("token") token: String): Observable<UserResponse>

    @FormUrlEncoded
    @POST("loginByPass/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun loginByPassword(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Observable<AuthResponse>

    @FormUrlEncoded
    @POST("registerNewUser/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun registerNewUser(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("surname") surname: String,
        @Field("phone") phone: String
    ): Observable<AuthResponse>

    @FormUrlEncoded
    @POST("smsRequest/")
    @Headers("User-Agent: Center Krasoty Https Nitron Apps Burzhua Web Connector")
    fun smsRequest(
        @Field("login") login: String,
        @Field("code") code: Int
    ): Observable<SMSResponse>
}