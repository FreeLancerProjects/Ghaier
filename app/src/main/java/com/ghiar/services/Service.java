package com.ghiar.services;

import com.ghiar.models.AuctionModel;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.PlaceGeocodeData;
import com.ghiar.models.PlaceMapDetailsData;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.ServiceModel;
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
    Call<ServiceModel> getHomeServices();

    @GET("api/get-mark")
    Call<MarkModel> getMarks();

    @GET("api/slider")
    Call<SliderModel> getHomeSliderData();

    @GET("api/get-market-services")
    Call<ServiceCenterModel> getServiceCenterData(@Query("services_id") int services_id);


    @GET("api/get-accessory")
    Call<ProductModel> getAccessories(
            @Query("paginate") String paginate);

    @GET("api/get-part")
    Call<ProductModel> getParts(
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
    @POST("api/contact-us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("phone") String phone,
                                   @Field("message") String message

    );
}