package com.mita.gamebuddymobile.api


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @POST("login")
    fun login(@Body login: Login): Call<LoginResponse>

    @POST("register")
    fun register(@Body user: User): Call<User>

    @GET("list")
    fun getUser(): Call<List<UserDataClass>>

    @GET("conversations/{conversationId}/messages")
    fun getMessages(@Path("conversationId") conversationId: String, @Header("Authorization") authorization: String): Call<MessageResponse>

    @POST("conversations/{conversationId}/messages")
    fun sendMessage(@Path("conversationId") conversationId: String, @Header("Authorization") authorization: String, @Body request: Map<String, String>): Call<Message>

    @GET("conversations/{conversationId}")
    fun getConversation(@Path("conversationId") conversationId: String, @Header("Authorization") authorization: String): Call<ConversationResponse>

    @POST("conversations/{conversationId}/message")
    fun postConversation(@Path("conversationId") conversationId: String, @Header("Authorization") authorization: String): Call<ConversationResponse>
    @PUT("users/{userId}")
    fun updateUser(@Path("userId") userId: Int, @Body user: UpdateUser, @Header("Authorization") token: String): Call<Void>

    @PUT("update-interests/{userId}")
    fun updateInterests(@Path("userId") userId: String, @Body interestsRequest: InterestsRequest, @Header("Authorization") token: String): Call<Void>

    @DELETE("users/{userId}")
    fun deleteUser(@Path("userId") userId: String, @Header("Authorization") authToken: String): Call<Void>

    @POST("logout")
    fun logout(@Header("Authorization") token: String): Call<Void>

    @POST("submit-report")
    fun submitReport(@Body reportRequest: ReportRequest, @Header("Authorization") token: String): Call<ReportResponse>

    @GET("/random-user")
    fun startMatching(): Call<RandomUserDataClass>
}
