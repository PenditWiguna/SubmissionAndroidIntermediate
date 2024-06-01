package android.me.keenanstory.api

import android.me.keenanstory.componen.response.DataStoriesResponse
import android.me.keenanstory.componen.response.TableStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Apiservice {
    @POST("register")
    fun register(
        @Body regCredential : RegisterRequest
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun registerModePost(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): android.me.keenanstory.componen.response.RegisterResponse

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginModePost(
        @Field("email") email: String,
        @Field("password") password: String
    ): android.me.keenanstory.componen.response.LoginResponse

    @GET("stories")
    suspend fun listStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("Location") location: Int=0
    ): TableStoriesResponse

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddResponse>

    @Multipart
    @POST("stories")
    suspend fun addFieldStories(
        @Header("Authorization") Authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat:RequestBody?=null,
        @Part("lon") lon:RequestBody?=null
    ): DataStoriesResponse

    @GET("stories")
    suspend fun getStoriesWiththePosition(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): TableStoriesResponse
}