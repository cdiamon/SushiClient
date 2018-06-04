package net.pdmtrdv.sashimisake

import io.reactivex.Observable
import net.pdmtrdv.sashimisake.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SashimiApiService {

    companion object {
        fun create(): SashimiApiService {

            val interceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl("https://electronic-menu-backend.appspot.com/")
                    .build()

            return retrofit.create(SashimiApiService::class.java)
        }
    }

    /***************
     ***************        IMAGES        ***************/

    @Multipart
    @Headers("x-auth-password:123456")
    @POST("images/upload")
    fun uploadImage(@Part imageMultipart: MultipartBody.Part):
            Observable<ImageUploadResponse>

//    @Multipart
//    @Headers("x-auth-password:123456")
//    @POST("images/upload")
////    fun uploadImage(@Part("file\"; filename=\"pp.png\" ") imageMultipart: MultipartBody.Part):
//    fun uploadImage(@Part("file\"; filename=\"pp.png\" ") imageMultipart: MultipartBody.Part):
//            Observable<ImageUploadResponse>

    /***************
     ***************        MENU        ***************/

    @GET("menu/categories")
    fun getMenuAllCategories():
            Observable<List<MenuCategoryResponse>>

    @POST("menu/category")
    @Headers("x-auth-password:123456")
    fun addMenuCategory(@Body menuAddCategoryRequest: MenuAddCategoryRequest):
            Observable<MenuCategoryResponse>

    @POST("menu/category/{categoryId}")
    @Headers("x-auth-password:123456")
    fun addDishToCategory(@Path("categoryId") categoryId: Int,
                          @Body menuAddCategoryRequest: MenuAddDishRequest):
            Observable<MenuDishResponse>

    @PUT("menu/category/{categoryId}/{dishId}")
    @Headers("x-auth-password:123456")
    fun updateDishInCategory(@Path("categoryId") categoryId: Int,
                             @Path("dishId") dishId: Int,
                             @Body dishUpdateRequest: DishUpdateRequest):
            Observable<MenuDishResponse>

    @GET("menu/category/{id}")
    fun getMenuCategory(@Path("id") id: Int):
            Observable<MenuCategoryResponse>

    @PUT("menu/category/{id}")
    @Headers("x-auth-password:123456")
    fun updateMenuCategory(@Path("id") id: Int,
                           @Body categoryUpdateRequest: CategoryUpdateRequest):
            Observable<MenuCategoryResponse>

    /***************
     ***************        ORDERS        ***************/

    /**
     *     may send orders @Query COOKING, READY, DONE for filter
     */
    @GET("orders")
    fun getOrdersList():
            Observable<List<Model.OrdersResponse>>

    @POST("orders")
    @Headers("x-auth-password:123456")
    fun createNewOrder(@Body orderList: List<Order>):
            Observable<Model.OrdersResponse>

    @PUT("orders/status/{id}")
    @Headers("x-auth-password:123456")
    fun changeOrderStatus(@Path("id") id: Int):
            Observable<Model.OrdersResponse>

    /***************
     ***************        REPORTS        ***************/

    @GET("reports/order")
    @Headers("x-auth-password:123456")
    fun getOrdersReport():
            Observable<List<ReportResponse>>

}








