package com.ghiar.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.ghiar.models.Create_Order_Model;
import com.ghiar.models.UserModel;
import com.ghiar.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }


    public void setIsLanguageSelected(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("language_selected", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("selected", true);
        editor.apply();
    }




    public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel.User getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel.User userModel = gson.fromJson(user_data, UserModel.User.class);
        return userModel;
    }
    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }

    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }


    public void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
        create_update_session(context, Tags.session_logout);
    }


    public void create_update_userData(Context context , UserModel userModel)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);
    }


  /*  public void create_update_cart(Context context , CreateOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_data", cart_data);
        editor.apply();

    }*/

  /*  public CreateOrderModel getCartData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_data","");
        Gson gson = new Gson();
        CreateOrderModel model = gson.fromJson(json_data,CreateOrderModel.class);
        return model;
    }*/

    public void clearCart(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }

    public void create_update_order(Context context, Create_Order_Model buy_models){
        SharedPreferences sharedPreferences=context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String user_order=gson.toJson(buy_models);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user_order",user_order);
        editor.apply();
        editor.commit();
    }
    public Create_Order_Model getUserOrder(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_order = preferences.getString("user_order",null);
        Type type=new TypeToken<Create_Order_Model>(){}.getType();
        Create_Order_Model buy_models=gson.fromJson(user_order,type);
        return buy_models;
    }

}