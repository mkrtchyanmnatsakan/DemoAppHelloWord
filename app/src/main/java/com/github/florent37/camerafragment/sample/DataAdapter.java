package com.github.florent37.camerafragment.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by m-dev on 3/29/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    private String  [] android;
    private Activity context;
    private String mtype;

    public DataAdapter(Activity context,String [] android,String type) {
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


       ;
        InputStream is = null;
        try {
            is = context.getAssets().open(mtype+android[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        viewHolder.img_android.setImageBitmap(bitmap);

        viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("img_name", mtype+android[i]);
                SharedPreferenceHelper.setSharedPreferenceString(context,ConstantValues.CURRANT_PATH,mtype+android[i]);
                context.setResult(10);
                context.onBackPressed();



            }
        });
        // viewHolder.img_android.setImageResource(android[i]);

    }

    @Override
    public int getItemCount() {
        return android.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_android;
        public ViewHolder(View view) {
            super(view);


            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}
