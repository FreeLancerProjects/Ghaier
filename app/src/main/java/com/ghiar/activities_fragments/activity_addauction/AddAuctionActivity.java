package com.ghiar.activities_fragments.activity_addauction;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ghiar.R;
import com.ghiar.adapters.ImagesAdapter;
import com.ghiar.databinding.ActivityAddAuctionBinding;
import com.ghiar.databinding.ActivityServicesCenterBinding;
import com.ghiar.databinding.DialogSelectImageBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.language.Language;
import com.ghiar.models.AddAuctionModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAuctionActivity extends AppCompatActivity implements Listeners.BackListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivityAddAuctionBinding binding;
    private String lang;

    private Preferences preferences;
    private UserModel userModel;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url, uri ;
    private List<Uri> urlList;
    private LinearLayoutManager manager;
    private ImagesAdapter imagesAdapter;
    private int type;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String date;
    private String time;
    private Calendar calendar1;
    private AddAuctionModel addAuctionModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_auction);
        initView();
    }


    private void initView() {
        addAuctionModel = new AddAuctionModel();
        urlList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setModel(addAuctionModel);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.imageSelectPhoto.setOnClickListener(view -> {
            type = 1;
            CreateImageAlertDialog();
        });
        binding.imageFill.setOnClickListener(view -> {
            type = 2;
            CreateImageAlertDialog();
        });
        binding.fldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                datePickerDialog.show(getFragmentManager(), "");

            }
        });
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        binding.recView.setLayoutManager(manager);
        imagesAdapter = new ImagesAdapter(urlList, this);
        binding.recView.setAdapter(imagesAdapter);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel != null) {
                    if (addAuctionModel.isDataValid(AddAuctionActivity.this)) {
                        if (uri != null) {
                            if (urlList != null && urlList.size() > 0) {
                                auctionimage(addAuctionModel);
                            } else {
                                auctionwithoutimages(addAuctionModel);
                            }
                        }
                        else {
                            Toast.makeText(AddAuctionActivity.this, getResources().getString(R.string.add_main_image), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                      //  Toast.makeText(AddAuctionActivity.this,"dlldkdkdk",Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Common.CreateNoSignAlertDialog(AddAuctionActivity.this);

                }
            }
        });
        createDatePickerDialog();
        createTimePickerDialog();
    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void auctionimage(AddAuctionModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(addAuctionModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(addAuctionModel.getArabic_title());
            RequestBody englishd_part = Common.getRequestBodyText(addAuctionModel.getEnglish_detials());
            RequestBody arabicd_part = Common.getRequestBodyText(addAuctionModel.getArabic_detials());
            RequestBody min_part = Common.getRequestBodyText(addAuctionModel.getMin());
            RequestBody quantity_part = Common.getRequestBodyText(addAuctionModel.getQuantity());
            RequestBody price_part = Common.getRequestBodyText(addAuctionModel.getPrice());
            RequestBody time_part = Common.getRequestBodyText(addAuctionModel.getTime());
            RequestBody date_part = Common.getRequestBodyText(addAuctionModel.getDate());
            RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");


            MultipartBody.Part imagepart = Common.getMultiPart(this, uri, "main_image");


            List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "Auction_images[]");
            Api.getService(Tags.base_url)
                    .auctionwithimage(arabic_part, english_part, arabicd_part, englishd_part, min_part, quantity_part, price_part, time_part, date_part, user_part, imagepart, partimageList)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddAuctionActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();

                                Appply();
                            } else {
                                try {

                                    Log.e("errorssss", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
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
                                        Toast.makeText(AddAuctionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAuctionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void auctionwithoutimages(AddAuctionModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(addAuctionModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(addAuctionModel.getArabic_title());
            RequestBody englishd_part = Common.getRequestBodyText(addAuctionModel.getEnglish_detials());
            RequestBody arabicd_part = Common.getRequestBodyText(addAuctionModel.getArabic_detials());
            RequestBody min_part = Common.getRequestBodyText(addAuctionModel.getMin());
            RequestBody quantity_part = Common.getRequestBodyText(addAuctionModel.getQuantity());
            RequestBody price_part = Common.getRequestBodyText(addAuctionModel.getPrice());
            RequestBody time_part = Common.getRequestBodyText(addAuctionModel.getTime());
            RequestBody date_part = Common.getRequestBodyText(addAuctionModel.getDate());
            RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");


            MultipartBody.Part imagepart = Common.getMultiPart(this, uri, "main_image");


            Api.getService(Tags.base_url)
                    .auctionwithimage(arabic_part, english_part, arabicd_part, englishd_part, min_part, quantity_part, price_part, time_part, date_part, user_part, imagepart)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddAuctionActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();

                                Appply();
                            } else {
                                try {

                                    Log.e("errorssss", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
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
                                        Toast.makeText(AddAuctionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAuctionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void Appply() {
        addAuctionModel = new AddAuctionModel();
        binding.setModel(addAuctionModel);
        binding.imageFill.setImageDrawable(null);
        binding.llimage.setVisibility(View.VISIBLE);
        urlList.clear();
        imagesAdapter.notifyDataSetChanged();
        finish();
    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar1 = calendar;

        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        //Log.e("kkkk", calendar.getTime().getMonth() + "");

        date = year + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        // date = calendar.get(Calendar.YEAR) + "-" + (calendar.getTime().getMonth()+calendar.getTime().getMonth():calendar.getTime().getMonth()) + "-" + (calendar.getTime().getDay()<10?"0"+calendar.getTime().getDay():calendar.getTime().getDay());
        timePickerDialog.show(getFragmentManager(), "");
        addAuctionModel.setDate(date);

    }

    private void createTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setLocale(new Locale(lang));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        time = dateFormat.format(calendar.getTime());
        addAuctionModel.setTime(time);
        binding.tvdate.setText(date + "  " + time);
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            if (type == 1) {
                urlList.add(url);
                imagesAdapter.notifyDataSetChanged();
            } else {
                uri = url;
                binding.llimage.setVisibility(View.GONE);
                Picasso.get().load(url).into(binding.imageFill);
            }

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            if (type == 1) {
                urlList.add(url);
                imagesAdapter.notifyDataSetChanged();
            } else {
                uri = url;
                binding.llimage.setVisibility(View.GONE);
                Picasso.get().load(url).into(binding.imageFill);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imagesAdapter.notifyItemRemoved(adapterPosition);

    }

    @Override
    public void back() {
//
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }


}
