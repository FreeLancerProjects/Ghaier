package com.ghiar.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_addauction.AddAuctionActivity;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activtity_auction_detials.AuctionDetialsActivity;
import com.ghiar.adapters.AuctionAdapter;
import com.ghiar.databinding.DialogAddPriceBinding;
import com.ghiar.databinding.DialogAlertBinding;
import com.ghiar.databinding.FragmentAuctionBinding;
import com.ghiar.databinding.FragmentMoreBinding;
import com.ghiar.models.AuctionModel;
import com.ghiar.models.ProductModel;
import com.ghiar.models.SingleAuctionModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ghiar.tags.Tags.base_url;

public class Fragment_Auction extends Fragment {

    private HomeActivity activity;
    private FragmentAuctionBinding binding;
    private Preferences preferences;
    private UserModel.User userModel;
    private String lang;
    private List<SingleAuctionModel> singleAuctionModelList;
    private AuctionAdapter auctionAdapter;

    public static Fragment_Auction newInstance() {

        return new Fragment_Auction();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        singleAuctionModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        userModel = preferences.getUserData(activity);
        auctionAdapter = new AuctionAdapter(activity, singleAuctionModelList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(auctionAdapter);
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAuctionActivity.class);
                startActivity(intent);
            }
        });
        getAuctions();
    }

    private void getAuctions() {
        Api.getService(base_url).getAuctions("off").enqueue(new Callback<AuctionModel>() {
            @Override
            public void onResponse(Call<AuctionModel> call, Response<AuctionModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getAuction() != null) {
                    singleAuctionModelList.clear();
                    singleAuctionModelList.addAll(response.body().getAuction());
                    if (response.body().getAuction().size() > 0) {
                        // rec_sent.setVisibility(View.VISIBLE);
                        //  Log.e("data",response.body().getData().get(0).getAr_title());

                        // binding.llNoStore.setVisibility(View.GONE);
                        auctionAdapter.notifyDataSetChanged();
                        //   total_page = response.body().getMeta().getLast_page();

                    } else {
                        auctionAdapter.notifyDataSetChanged();

                        //  binding.llNoStore.setVisibility(View.VISIBLE);

                    }
                } else {
                    auctionAdapter.notifyDataSetChanged();

                    // binding.llNoStore.setVisibility(View.VISIBLE);

                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AuctionModel> call, Throwable t) {
                //   Log.d(TAG, t.getMessage());
                binding.progBar.setVisibility(View.GONE);
            }
        });

    }

    public void CreateDialogAlert(Context context, int pos) {
        if (userModel != null) {
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .create();

            DialogAddPriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_price, null, false);

            binding.btnSend.setOnClickListener(v -> {
                        String value = binding.edtstprice.getText().toString();
                        if (!value.isEmpty()) {
                            sendauction(value, pos);
                            dialog.dismiss();

                        } else {
                            binding.edtstprice.setError(context.getResources().getString(R.string.field_req));
                        }
                    }

            );
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
            dialog.setCanceledOnTouchOutside(false);
            dialog.setView(binding.getRoot());
            dialog.show();
        } else {

        }
    }

    private void sendauction(String price, int pos) {
        try {

            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .sendAuction(price, singleAuctionModelList.get(pos).getId() + "", userModel.getId() + "")
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                getAuctions();
                            } else {

                                try {

                                    Log.e("errorcode", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    //   Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    if (response.code() == 422) {
                                        // Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();//
                                        try {
                                            Toast.makeText(activity, response.errorBody().string(), Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void show(int layoutPosition) {
        Intent intent = new Intent(activity, AuctionDetialsActivity.class);
        intent.putExtra("search", singleAuctionModelList.get(layoutPosition).getId() + "");
        startActivity(intent);
    }
}
