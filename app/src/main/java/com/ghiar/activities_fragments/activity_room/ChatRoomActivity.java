package com.ghiar.activities_fragments.activity_room;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ghiar.R;
import com.ghiar.activities_fragments.chat_activity.ChatActivity;
import com.ghiar.adapters.Room_Adapter;
import com.ghiar.databinding.ActivityChatRoomBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.ChatUserModel;
import com.ghiar.models.UserModel;
import com.ghiar.models.UserRoomModelData;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatRoomBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Room_Adapter room_adapter;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        initView();
        if(userModel!=null){
            getRooms();}

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
       binding.setBackListener(this);
       binding.setLang(lang);
        userRoomModelList=new ArrayList<>();
        room_adapter=new Room_Adapter(this,userRoomModelList);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(room_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_items = room_adapter.getItemCount();
                    int lastItemPos = ((GridLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    if (total_items>=6&&(lastItemPos==total_items-2)&&!isLoading)
                    {
                        isLoading = true;
                        userRoomModelList.add(null);
                        room_adapter.notifyItemInserted(userRoomModelList.size()-1);
                      //  loadMore(page);
                    }

                }
            }
        });
        getRooms();


    }
    public void getRooms()
    {
        userModel = preferences.getUserData(this);

        try {
            Api.getService(Tags.base_url)
                    .getRooms(userModel.getUser().getId())
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getRooms()!=null)
                            {
                                userRoomModelList.clear();
                                userRoomModelList.addAll(response.body().getRooms());
                                if (userRoomModelList.size()>0)
                                {
                                    room_adapter.notifyDataSetChanged();
                                    binding.tvNoConversation.setVisibility(View.GONE);
                                }else
                                {
                                    binding.tvNoConversation.setVisibility(View.VISIBLE);

                                }
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(ChatRoomActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(ChatRoomActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(ChatRoomActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){

                            }
                        }
                    });
        }catch (Exception e){
            Log.e("eeee",e.getMessage()+"__");
            binding.progBar.setVisibility(View.GONE);
        }
    }



//    private void loadMore(int page)
//    {
//        try {
//
//            Api.getService(Tags.base_url)
//                    .getRooms(userModel.getId(),page)
//                    .enqueue(new Callback<UserRoomModelData>() {
//                        @Override
//                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
//                            userRoomModelList.remove(userRoomModelList.size()-1);
//                            room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                            isLoading = false;
//
//                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
//                            {
//                                userRoomModelList.addAll(response.body().getData());
//                                room_adapter.notifyDataSetChanged();
//                                current_page = response.body().getCurrent_page();
//                            }else
//                            {
//                                if (response.code() == 500) {
//                                    Toast.makeText(ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                }else
//                                {
//                                    Toast.makeText(ChatRoomActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error",response.code()+"_"+response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
//                            try {
//                                if (userRoomModelList.get(userRoomModelList.size()-1)==null)
//                                {
//                                    userRoomModelList.remove(userRoomModelList.size()-1);
//                                    room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                                    isLoading = false;
//                                }
//
//
//                                if (t.getMessage()!=null)
//                                {
//                                    Log.e("error",t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
//                                    }else
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }catch (Exception e){}
//                        }
//                    });
//        }catch (Exception e){
//            binding.progBar.setVisibility(View.GONE);
//        }
//    }
    public void setItemData(UserRoomModelData.UserRoomModel userRoomModel, int adapterPosition) {

       // userRoomModel.setMy_message_unread_count(0);
        userRoomModelList.set(adapterPosition,userRoomModel);
        room_adapter.notifyItemChanged(adapterPosition);

        ChatUserModel chatUserModel = new ChatUserModel(userRoomModel.getName(),userRoomModel.getImage(),userRoomModel.getSecond_user_id(),userRoomModel.getId());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_user_data",chatUserModel);
        startActivityForResult(intent,1000);
    }


    @Override
    public void back() {
        finish();
    }

}
