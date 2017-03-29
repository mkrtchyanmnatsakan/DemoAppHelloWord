package com.github.florent37.camerafragment.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;

public class ImageListActivity extends AppCompatActivity {

    private String[] imageListIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        try {
            if(getIntent().hasExtra("type")){
                Log.e("rrr",getIntent().getStringExtra("type"));
                imageListIds = getAssets().list(getIntent().getStringExtra("type"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        initViews();
    }


    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);


        DataAdapter adapter = new DataAdapter(this,imageListIds,getIntent().getStringExtra("type")+"/");
        recyclerView.setAdapter(adapter);



    }
}
