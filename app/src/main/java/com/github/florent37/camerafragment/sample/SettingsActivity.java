package com.github.florent37.camerafragment.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {




    @Bind(R.id.beck_relativeLayout)
    RelativeLayout beckRelativeLayout;

    @Bind(R.id.instagram_layout)
    RelativeLayout instagramLayout;

    @Bind(R.id.twitter_layout)
    RelativeLayout twitterLayout;

    @Bind(R.id.pinteras_layout)
    RelativeLayout pinterasLayout;

    @Bind(R.id.youtube_layout)
    RelativeLayout youtubeLayout;

    @Bind(R.id.google_plus_layout)
    RelativeLayout googlePlusLayout;

    @Bind(R.id.facebook_layout)
    RelativeLayout facebookLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        setOnClick();
    }


    private void setOnClick(){
        beckRelativeLayout.setOnClickListener(this);
        instagramLayout.setOnClickListener(this);
        twitterLayout.setOnClickListener(this);
        pinterasLayout.setOnClickListener(this);
        youtubeLayout.setOnClickListener(this);
        googlePlusLayout.setOnClickListener(this);
        facebookLayout.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.beck_relativeLayout:

                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;


        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}
