package com.github.florent37.camerafragment.sample;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static String FACEBOOK_URL = "https://www.facebook.com/copyart2017";
    public static String FACEBOOK_PAGE_ID = "copyart2017";




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

    @Bind(R.id.page_layout)
    RelativeLayout pageLayout;

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
        pageLayout.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.beck_relativeLayout:

                finish();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                break;

            case R.id.twitter_layout:

                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/copyart0"));
                }
                this.startActivity(intent);

                break;

            case R.id.instagram_layout:

                Uri uri = Uri.parse("https://www.instagram.com/_copyart/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/_copyart/")));
                }

                break;

            case R.id.facebook_layout:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;

            case R.id.pinteras_layout:


                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://www.pinterest.com/copyart/")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pinterest.com/copyart/")));
                }
                break;

            case R.id.google_plus_layout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/107689029097566394219")));
                    break;

            case R.id.page_layout:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.copyart.co/")));
                break;

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    }


    private String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
}
