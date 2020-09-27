package com.ghiar.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.ghiar.R;
import com.ghiar.activities_fragments.activity_home.HomeActivity;
import com.ghiar.activities_fragments.activity_signup.SignUpActivity;
import com.ghiar.adapters.CityAdapter;
import com.ghiar.adapters.MarkAdapter;
import com.ghiar.adapters.MarksAdapter;
import com.ghiar.adapters.ModelsAdapter;
import com.ghiar.databinding.FragmentRequiredBinding;
import com.ghiar.interfaces.Listeners;
import com.ghiar.models.AddWantedModel;
import com.ghiar.models.CityDataModel;
import com.ghiar.models.MarkModel;
import com.ghiar.models.ModelModel;
import com.ghiar.models.UserModel;
import com.ghiar.preferences.Preferences;
import com.ghiar.remote.Api;
import com.ghiar.share.Common;
import com.ghiar.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Reguired extends Fragment implements Listeners.AddRequiredListener{

    private HomeActivity activity;
    private FragmentRequiredBinding binding;
    private Preferences preferences;
    private UserModel.User userModel;
    private AddWantedModel addWantedModel;
    private String lang;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 4, IMG_REQ3 = 5;
    private Uri imgUri1 = null;
    private Uri uri = null;
    private int selectedType;
    private List<MarkModel> markModelList;
    private MarksAdapter marksAdapter;
    private MarkModel markModel;
    private List<ModelModel> modelModelList;
    private ModelsAdapter modelsAdapter;
    private ModelModel modelModel;

    public static Fragment_Reguired newInstance() {

        return new Fragment_Reguired();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_required, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        markModelList = new ArrayList<>();
        modelModelList = new ArrayList<>();
        addWantedModel=new AddWantedModel();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel = preferences.getUserData(activity);
        marksAdapter = new MarksAdapter(markModelList, activity);
        binding.spinnerMarks.setAdapter(marksAdapter);
        markModel = new MarkModel();
        markModel.setId(0);
        markModel.setTitle_ar(getString(R.string.choose));
        markModelList.add(markModel);
        modelsAdapter = new ModelsAdapter(modelModelList, activity);
        binding.spinnerModel.setAdapter(modelsAdapter);
        modelModel = new ModelModel();
        modelModel.setId(0);
        modelModel.setTitle_ar(getString(R.string.choose));
        modelModelList.add(modelModel);


        binding.spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    modelModel.setId(0);
                } else {
                    modelModel.setId(modelModelList.get(position).getId());

                }
                binding.setModelsmodel(modelModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    markModel.setId(0);
                } else {
                    markModel.setId(markModelList.get(position).getId());

                }
                binding.setMarksmodel(markModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getMarks();
        getModels();
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("bbbb","  "+userModel.getId());
                checkDataValid();
            }
        });

        binding.imageFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ1);
            }
        });
    }

    private void getMarks() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .getMarks()
                .enqueue(new Callback<MarkModel>() {
                    @Override
                    public void onResponse(Call<MarkModel> call, Response<MarkModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getMarks().size() > 0) {
                                markModelList.clear();
                                markModelList.add(markModel);
                                markModelList.addAll(response.body().getMarks());
                                Log.e("data", markModelList.size() + "__");
                                activity.runOnUiThread(() -> {
                                    marksAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MarkModel> call, Throwable t) {
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
    }


    private void getModels() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .getModels()
                .enqueue(new Callback<ModelModel>() {
                    @Override
                    public void onResponse(Call<ModelModel> call, Response<ModelModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getModels().size() > 0) {
                                modelModelList.clear();
                                modelModelList.add(modelModel);
                                modelModelList.addAll(response.body().getModels());
                                Log.e("data", markModelList.size() + "__");
                                activity.runOnUiThread(() -> {
                                    modelsAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelModel> call, Throwable t) {
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
    }


    private void addWanted() {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody title_ar_part = Common.getRequestBodyText(String.valueOf(addWantedModel.getTitle_ar()));
        RequestBody title_en_part = Common.getRequestBodyText(String.valueOf(addWantedModel.getTitle_en()));
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody model_id_part = Common.getRequestBodyText(addWantedModel.getModel_id());
        RequestBody mark_id_part = Common.getRequestBodyText(addWantedModel.getMark_id());
        RequestBody status_part = Common.getRequestBodyText(addWantedModel.getStatus());
        RequestBody amount_part = Common.getRequestBodyText(addWantedModel.getAmount());
        RequestBody type_part = Common.getRequestBodyText(addWantedModel.getType());
        MultipartBody.Part image = Common.getMultiPart(activity, addWantedModel.getImage(), "image");


        Api.getService(Tags.base_url)
                .addWanted(title_ar_part, title_en_part, user_id_part, model_id_part, mark_id_part, amount_part, status_part, type_part, image)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            activity.finish();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("error", response.code() + "__" + response.errorBody() + "");

                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addًWantedswithoutImage() {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody title_ar_part = Common.getRequestBodyText(String.valueOf(addWantedModel.getTitle_ar()));
        RequestBody title_en_part = Common.getRequestBodyText(String.valueOf(addWantedModel.getTitle_en()));
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody model_id_part = Common.getRequestBodyText(addWantedModel.getModel_id());
        RequestBody mark_id_part = Common.getRequestBodyText(addWantedModel.getMark_id());
        RequestBody status_part = Common.getRequestBodyText(addWantedModel.getStatus());
        RequestBody amount_part = Common.getRequestBodyText(addWantedModel.getAmount());
        RequestBody type_part = Common.getRequestBodyText(addWantedModel.getType());

        Log.e("ccccc",addWantedModel.getTitle_ar()+" " +addWantedModel.getTitle_en()+" "+ userModel.getId()+ " "+ addWantedModel.getModel_id()+" "+  " "+model_id_part+" " +status_part+" "+ amount_part+" "+type_part);
        Api.getService(Tags.base_url)
                .addWantedWithOutImage(user_id_part,title_ar_part, title_en_part, model_id_part, mark_id_part, amount_part, status_part, type_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            activity.finish();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("error", response.code() + "__" + response.errorBody() + "");

                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            if (uri != null) {
                Log.e("uri", uri + "_");
                addWantedModel.setImage(uri);
                String path = Common.getImagePath(activity, uri);
                if (path != null) {
                    Log.e("path", path + "__");

                    Picasso.get().load(new File(path)).fit().into(binding.imageFill);

                } else {
                    Picasso.get().load(uri).fit().into(binding.imageFill);

                }
                binding.image.setVisibility(View.GONE);
            }


        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            binding.image.setVisibility(View.GONE);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                String path = Common.getImagePath(activity, uri);

                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.imageFill);

                } else {
                    Picasso.get().load(uri).fit().into(binding.imageFill);

                }
            }


        }




    }


    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", ""));
    }


    @Override
    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    @Override
    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }
    @Override
    public void checkDataValid() {
       addWantedModel.setType(String.valueOf(binding.spinnertype.getSelectedItem()));
        if (binding.rbChoose1.isChecked()) {
            addWantedModel.setStatus("new");
        } else if (binding.rbChoose2.isChecked()) {
            addWantedModel.setStatus("old");

        }
        binding.setModel(addWantedModel);
        if (!addWantedModel.isDataValid(activity)) {
            Log.e("kkkkkk","5555");
            if (imgUri1 != null) {
                addWanted();
            } else {
                addًWantedswithoutImage();
            }
        }
    }

    @Override
    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    @Override
    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(activity, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void CreateImageAlertDialog(final int img_req) {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_image, null);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_gallery = view.findViewById(R.id.btn_gallery);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedType = 2;
                Check_CameraPermission(img_req);

            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedType = 1;
                CheckReadPermission(img_req);


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    private void CheckReadPermission(int img_req) {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, img_req);
        } else {
            SelectImage(1, img_req);
        }
    }

    private void Check_CameraPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(activity, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, img_req);
        } else {
            SelectImage(2, img_req);

        }

    }

    private void SelectImage(int type, int img_req) {

        Intent intent = new Intent();

        if (type == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (type == 2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

}
