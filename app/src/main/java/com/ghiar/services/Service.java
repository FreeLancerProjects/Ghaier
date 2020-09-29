package com.ghiar.services;

import com.ghiar.models.AuctionModel;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MainServiceModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.MarksModel;
import com.ghiar.models.ModelModel;
import com.ghiar.models.ModelsData;
import com.ghiar.models.NotificationDataModel;
import com.ghiar.models.PlaceGeocodeData;
import com.ghiar.models.PlaceMapDetailsData;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ProductsModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.ServiceCentersModel;
import com.ghiar.models.ServiceModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.SliderModel;
import com.ghiar.models.SettingModel;
import com.ghiar.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("api/get-main-services")
    Call<MainServiceModel> getHomeServices();

    @GET("api/get-mark")
    Call<MarksModel> getMarks();

    @GET("api/get-model")
    Call<ModelsData> getModels();

    @GET("api/slider")
    Call<SliderModel> getHomeSliderData();

    @GET("api/get-market-services")
    Call<ServiceCentersModel> getServiceCenterData(@Query("services_id") int services_id);

    @GET("api/one-market")
    Call<ServiceCenterModel> get_singleservicecenter(
            @Query("market_id") String market_id


    );

    @GET("api/get-accessory")
    Call<ProductsModel> getAccessories(
            @Query("paginate") String paginate);

    @GET("api/get-part")
    Call<ProductsModel> getParts(
            @Query("paginate") String paginate);

    @GET("api/get-auctions")
    Call<AuctionModel> getAuctions(
            @Query("paginate") String paginate);

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone
    );


    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("name") String name,
                           @Field("email") String email,
                           @Field("phone_code") String phone_code,
                           @Field("phone") String phone,
                           @Field("city_id") String city_id


    );

    @GET("api/city")
    Call<CityDataModel> getCity();

    @GET("api/setting")
    Call<SettingModel> getSetting();

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Header("user_id") int user_id,
                              @Field("phone_token") String phone_token


    );

    @FormUrlEncoded
    @POST("api/auction-action")
    Call<ResponseBody> sendAuction(
            @Field("price") String price,
            @Field("auction_id") String auction_id,
            @Field("value") String value


    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("phone") String phone,
                                   @Field("message") String message

    );

    @Multipart
    @POST("api/add-wanted")
    Call<ResponseBody> addWanted(@Part("user_id") RequestBody user_id,
                                 @Part("title_ar") RequestBody title_ar,
                                 @Part("title_en") RequestBody title_en,
                                 @Part("model_id") RequestBody model_id,
                                 @Part("mark_id") RequestBody mark_id,
                                 @Part("amount") RequestBody amount,
                                 @Part("status") RequestBody status,
                                 @Part("type") RequestBody type,
                                 @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/add-wanted")
    Call<ResponseBody> addWantedWithOutImage(@Part("user_id") RequestBody user_id,
                                             @Part("title_ar") RequestBody title_ar,
                                             @Part("title_en") RequestBody title_en,
                                             @Part("model_id") RequestBody model_id,
                                             @Part("mark_id") RequestBody mark_id,
                                             @Part("amount") RequestBody amount,
                                             @Part("status") RequestBody status,
                                             @Part("type") RequestBody type
    );

    @GET("api/get-one-auction")
    Call<SingleAuctionModel> get_singleauction(
            @Query("auction_id") String auction_id


    );
    @Multipart
    @POST("api/addNewAuctionRequest")
    Call<ResponseBody> auctionwithimage(
            @Part("title_ar") RequestBody title_ar,
            @Part("title_en") RequestBody title_en,
            @Part("details_ar") RequestBody details_ar,
            @Part("details_en") RequestBody details_en,
            @Part("step_price") RequestBody step_price,
            @Part("amount") RequestBody amount,
            @Part("start_price") RequestBody start_price,
            @Part("end_time") RequestBody end_time,

            @Part("end_date") RequestBody end_date,

            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part images,
            @Part List<MultipartBody.Part> image);
    @Multipart
    @POST("api/addNewAuctionRequest")
    Call<ResponseBody> auctionwithimage(
            @Part("title_ar") RequestBody title_ar,
            @Part("title_en") RequestBody title_en,
            @Part("details_ar") RequestBody details_ar,
            @Part("details_en") RequestBody details_en,
            @Part("step_price") RequestBody step_price,
            @Part("amount") RequestBody amount,
            @Part("start_price") RequestBody start_price,
            @Part("end_time") RequestBody end_time,

            @Part("end_date") RequestBody end_date,

            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part images);
    @GET("api/my-notifications")
    Call<NotificationDataModel> getnotification(
            @Query("page") int page,
            @Header("Authorization") String Authorization,
            @Header("lang") String lang

    );

}