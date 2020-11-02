package com.ghiar.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.ghiar.R;
import com.ghiar.models.UserModel;
import com.ghiar.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }
    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView,String url)
    {
        Picasso.get().load(Uri.parse(url)).fit().into(imageView);

    }
    @BindingAdapter("imageProfile")
    public static void displayImageProfile(View view ,String imageEndPoint)
    {
        String path = Tags.IMAGE_URL+imageEndPoint;
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;
            Picasso.get().load(Uri.parse(path)).placeholder(R.drawable.ic_avatar).into(imageView);
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;
            Picasso.get().load(Uri.parse(path)).placeholder(R.drawable.ic_avatar).into(imageView);
        }else if (view instanceof ImageView)
        {

            ImageView imageView = (ImageView) view;
            Picasso.get().load(Uri.parse(path)).placeholder(R.drawable.ic_avatar).into(imageView);
        }
    }
    @BindingAdapter("rate")
    public static void rate(SimpleRatingBar simpleRatingBar, double rate) {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget((float) rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }








    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }
    @BindingAdapter({"date"})
    public static void displayDate (TextView textView,long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy ,HH:mm ", Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));

        textView.setText(String.format(":"+m_date));

    }
    @BindingAdapter({"order_status"})
    public static void orderStatus(TextView textView, String status) {
      if (status.equals("new_order")) {
            textView.setText(textView.getContext().getString(R.string.new_order));
        } else if (status.equals("driver_accept")) {
            textView.setText(textView.getContext().getString(R.string.accepted));

        } else if (status.equals("driver_delivery")) {
            textView.setText(textView.getContext().getString(R.string.in_way));

        } else if (status.equals("driver_refuser")) {
            textView.setText(textView.getContext().getString(R.string.refused));

        } else if (status.equals("driver_end")) {
            textView.setText(textView.getContext().getString(R.string.completed));

        }

    }
}










