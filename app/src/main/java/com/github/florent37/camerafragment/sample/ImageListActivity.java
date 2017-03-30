package com.github.florent37.camerafragment.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;

public class ImageListActivity extends AppCompatActivity  implements View.OnClickListener{

    private String[] imageListIds;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private DataAdapter adapter;
    private RelativeLayout beckRelativeLayout;

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
         recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DataAdapter(this,imageListIds,getIntent().getStringExtra("type")+"/");
        recyclerView.setAdapter(adapter);

        beckRelativeLayout = (RelativeLayout) findViewById(R.id.beck_relativeLayout);
        beckRelativeLayout.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.beck_relativeLayout:
                onBackPressed();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }

    }
}
