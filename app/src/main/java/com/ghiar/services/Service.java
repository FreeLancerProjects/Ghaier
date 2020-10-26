package com.ghiar.services;

import com.ghiar.models.AuctionModel;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.MainServiceModel;
import com.ghiar.models.MarkDataInModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.MarksDataModel;
import com.ghiar.models.MarksModel;
import com.ghiar.models.MessageDataModel;
import com.ghiar.models.MessageModel;
import com.ghiar.models.ModelModel;
import com.ghiar.models.ModelsData;
import com.ghiar.models.MyAuctionModel;
import com.ghiar.models.MyRequiredModel;
import com.ghiar.models.NotificationDataModel;
import com.ghiar.models.OrderModel;
import com.ghiar.models.Order_Model;
import com.ghiar.models.PlaceDirectionModel;
import com.ghiar.models.PlaceGeocodeData;
import com.ghiar.models.PlaceMapDetailsData;
import com.ghiar.models.ProductModel;
import com.ghiar.models.ProductsModel;
import com.ghiar.models.RoomIdModel;
import com.ghiar.models.ServiceCenterModel;
import com.ghiar.models.ServiceCentersModel;
import com.ghiar.models.ServiceModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.SliderModel;
import com.ghiar.models.SettingModel;
import com.ghiar.models.UserModel;
import com.ghiar.models.UserRoomModelData;

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

    @GET("directions/json")
    Call<PlaceDirectionModel> getDirection(@Query("origin") String origin,
                                           @Query("destination") String destination,
                                           @Query("transit_mode") String transit_mode,
                                           @Query("key") String key
    );

    @GET("api/get-main-services")
    Call<MainServiceModel> getHomeServices();

    @GET("api/get-mark")
    Call<MarksModel> getMarks();

    @GET("api/get-model")
    Call<ModelsData> getModels();

    @GET("api/slider")
    Call<SliderModel> getHomeSliderData();

    @GET("api/market-filtter")
    Call<ServiceCentersModel> getServiceCenterData(
            @Query("mark_id") String mark_id,
            @Query("city_id") String city_id,
            @Query("name") String name,
            @Query("services_id") String services_id,
            @Query("google_lat") Double google_lat,
            @Query("google_long") Double google_long
    );

    @GET("api/one-market")
    Call<ServiceCentersModel> get_singleservicecenter(
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
            @Field("value") String value,
            @Field("auction_id") String auction_id,
            @Field("user_id") String user_id


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

    @GET("api/advertsment")
    Call<MarksDataModel> get_singleadversiment(
            @Query("advertsment_id") String advertsment_id


    );

    @Multipart
    @POST("api/add-auction")
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
    @POST("api/add-auction")
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

    @GET("api/my-notification")
    Call<NotificationDataModel> getnotification(

            @Query("user_id") String user_id
    );

    @GET("api/mark-filter")
    Call<MarksDataModel> get_MarkDataIn(
            @Query("country_id") String country_id,
            @Query("model_id") String model_id,
            @Query("status") String status,
            @Query("title") String title,
            @Query("type") String type,
            @Query("mark_id") String mark_id


    );

    @POST("api/store-order")
    Call<ResponseBody> accept_orders(
            @Body Create_Order_Model add_order_model);

    @FormUrlEncoded
    @POST("api/my-rooms")
    Call<UserRoomModelData> getRooms(@Field("client_id") int client_id
    );

    @FormUrlEncoded
    @POST("api/single-chat-room")
    Call<MessageDataModel> getRoomMessages(
            @Field("room_id") int room_id
    );


    @FormUrlEncoded
    @POST("api/send-messgae")
    Call<MessageModel> sendmessagetext(
            @Field("from_id") String from_id,

            @Field("to_id") String to_id,
            @Field("type") String type,
            @Field("room_id") String room_id,
            @Field("message") String message


    );

    @FormUrlEncoded
    @POST("api/chatRoom/get")
    Call<RoomIdModel> getRoomId(@Field("from_user_id") int from_user_id,
                                @Field("to_user_id") int to_user_id


    );

    @Multipart
    @POST("api/send-messgae")
    Call<MessageModel> sendmessagewithimage
            (
                    @Part("from_id") RequestBody from_id,

                    @Part("to_id") RequestBody to_id,
                    @Part("type") RequestBody type,
                    @Part("room_id") RequestBody room_id,

                    @Part MultipartBody.Part imagepart

//
            );

    @GET("api/get-my-auction")
    Call<MyAuctionModel> getmyauction(
            @Query("user_id") String user_id

    );

    @GET("api/my-wanted")
    Call<MyRequiredModel> getclentcurrentrequired(
            @Query("user_id") String user_id


    );

    @GET("api/my-order")
    Call<Order_Model> getclentcurrentorder(
            @Query("user_id") String user_id,
            @Query("order_type") String order_type


    );

    @GET("api/search")
    Call<MarksDataModel> searchByName(@Query("search") String search,
                                      @Query("paginate") String paginate

    );

    @GET("api/one-order")
    Call<OrderModel> order_detials(@Query("order_id") int order_id);

    @FormUrlEncoded
    @POST("api/RateProduct")
    Call<ResponseBody> rate(
            @Field("adv_id") String adv_id,
            @Field("user_id") String user_id,
            @Field("value") Double value
    );

    @FormUrlEncoded
    @POST("api/store-token")
    Call<ResponseBody> updateToken(
            @Field("user_id") int user_id,
            @Field("token") String token,
            @Field("type") String type
    );
}
