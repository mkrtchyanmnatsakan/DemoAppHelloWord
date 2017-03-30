package com.github.florent37.camerafragment.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by m-dev on 3/29/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private String[] android;
    private Activity context;
    private String mtype;

    public DataAdapter(Activity context, String[] android, String type) {
        this.mtype = type;
        this.android = android;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context)
                .load("file:///android_asset/" + mtype + android[i])
                .resize(150,150)
                .into(viewHolder.img_android);

        viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("img_name", mtype+android[i]);
                SharedPreferenceHelper.setSharedPreferenceString(context,ConstantValues.CURRANT_PATH,mtype+android[i]);
                context.setResult(10);
                context.onBackPressed();



            }
        });
      }

    @Override
    public int getItemCount() {
        return android.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_android;

        public ViewHolder(View view) {
            super(view);


            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}
