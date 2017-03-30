package com.github.florent37.camerafragment.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyAlbumActivity extends AppCompatActivity implements View.OnClickListener {

ImageView beckImageView;
    RelativeLayout cartoonRelativeLayout;
    RelativeLayout portraitRelativeLayout;
    RelativeLayout architectureRelativeLayout;
    RelativeLayout animalsRelativeLayout;
    RelativeLayout treeDRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_album);

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==10){
            setResult(10);
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.beck_imageView:
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.cartoon_relativeLayout:
                Toast.makeText(this, "Go to cartoon album", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MyAlbumActivity.this,ImageListActivity.class).putExtra("type","cartoon"),2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.portrait_relativeLayout:
                Toast.makeText(this, "Go to portrait album", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MyAlbumActivity.this,ImageListActivity.class).putExtra("type","portrait"),2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.architecture_relativeLayout:
                Toast.makeText(this, "Go to architecture album", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MyAlbumActivity.this,ImageListActivity.class).putExtra("type","architecture"),2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case R.id.animals_relativeLayout:
                Toast.makeText(this, "Go to animals album", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MyAlbumActivity.this,ImageListActivity.class).putExtra("type","animals"),2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.tree_D_relativeLayout:
                Toast.makeText(this, "Go to 3D album", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(MyAlbumActivity.this,ImageListActivity.class).putExtra("type","3d"),2);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    private void initView(){
        beckImageView = (ImageView) findViewById(R.id.beck_imageView);
        beckImageView.setOnClickListener(this);

        cartoonRelativeLayout = (RelativeLayout) findViewById(R.id.cartoon_relativeLayout);
        cartoonRelativeLayout.setOnClickListener(this);

        portraitRelativeLayout = (RelativeLayout) findViewById(R.id.portrait_relativeLayout);
        portraitRelativeLayout.setOnClickListener(this);

        architectureRelativeLayout = (RelativeLayout) findViewById(R.id.architecture_relativeLayout);
        architectureRelativeLayout.setOnClickListener(this);

        animalsRelativeLayout = (RelativeLayout) findViewById(R.id.animals_relativeLayout);
        animalsRelativeLayout.setOnClickListener(this);

        treeDRelativeLayout = (RelativeLayout) findViewById(R.id.tree_D_relativeLayout);
        treeDRelativeLayout.setOnClickListener(this);

    }
}
