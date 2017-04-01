package com.github.florent37.camerafragment.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordTextAdapter;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.MediaActionSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    //zoom , move and rotation imageView
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private float[] lastEvent;
    private float d;
    private float newRot;
    //////////////////////////
    public static final String FRAGMENT_TAG = "camera";
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private boolean clickStart;

    private int widthDisplay;
    private int heightDisolay;



    @Bind(R.id.settings_view)
    CameraSettingsView settingsView;

    @Bind(R.id.flash_switch_view)
    FlashSwitchView flashSwitchView;

    @Bind(R.id.front_back_camera_switcher)
    CameraSwitchView cameraSwitchView;

    @Bind(R.id.record_button)
    RecordButton recordButton;

    @Bind(R.id.photo_video_camera_switcher)
    MediaActionSwitchView mediaActionSwitchView;

    @Bind(R.id.record_duration_text)
    TextView recordDurationText;

    @Bind(R.id.record_size_mb_text)
    TextView recordSizeText;

    @Bind(R.id.cameraLayout)
    View cameraLayout;

    @Bind(R.id.addCameraButton)
    View addCameraButton;

    @Bind(R.id.transparet_efect_imageView)
    ImageView transparentEffectImg;

    @Bind(R.id.flash_switch_camera_view)
    LinearLayout flashSwitchCameraView;

    @Bind(R.id.start_cancel_layout)
    LinearLayout startCancelLayout;

    @Bind(R.id.start_relativeLayout)
    RelativeLayout startRelativeLayout;

    @Bind(R.id.effect_layout)
    RelativeLayout effectLayout;

    @Bind(R.id.cancel_relativeLayout)
    RelativeLayout cancelRelativeLayout;

    @Bind(R.id.ready_relativeLayout)
    RelativeLayout readyLayout;

    @Bind(R.id.opacity)
    SeekBar opacityBar;

    @Bind(R.id.seekbar_relative_layout)
    RelativeLayout seekbarLayout;

    @Bind(R.id.camera_fab)
    FloatingActionButton photoFab;

    @Bind(R.id.my_settings_fab)
    FloatingActionButton settingsFab;

    @Bind(R.id.my_album_fab)
    FloatingActionButton albumFab;

    @Bind(R.id.my_album_art_fab)
    FloatingActionButton myAlbumArtFab;

    @Bind(R.id.menu)
    FloatingActionMenu menu;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.e("onCreate","run onCreate metod");

        if (Build.VERSION.SDK_INT > 15) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            } else addCamera();
        } else {
            addCamera();
        }


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setOnClick();
        transparentEffectImg.setOnTouchListener(this);


       /* transparentEffectImg.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        transparentEffectImg.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();*/
        //transparentEffectImg.setScaleType(ImageView.ScaleType.FIT_START);

        ImagePicker.setMinQuality(1000, 800);

        opacityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                transparentEffectImg.setAlpha((float) progress / 255);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        widthDisplay = getWindowManager().getDefaultDisplay().getWidth();
        heightDisolay = getWindowManager().getDefaultDisplay().getHeight();

    }

    @OnClick(R.id.flash_switch_view)
    public void onFlashSwitcClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.toggleFlashMode();

        }
    }

    @OnClick(R.id.front_back_camera_switcher)
    public void onSwitchCameraClicked() {

        Log.e("onSwitchCameraClicked", "true");

        flashSwitchCameraView.setVisibility(View.VISIBLE);
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {

            Log.e("cameraFragment","not null");
            cameraFragment.switchCameraTypeFrontBack();

        }
    }

    @OnClick(R.id.record_button)
    public void onRecordButtonClicked() {

        startCancelLayout.setVisibility(View.VISIBLE);
        flashSwitchCameraView.setVisibility(View.GONE);
        final CameraFragmentApi cameraFragment = getCameraFragment();
        recordButton.setVisibility(View.GONE);
        Log.e("clicked", "true**");
        if (cameraFragment != null) {
            Log.e("clicked", "true");
            cameraFragment.
                    takePhotoOrCaptureVideo(
                            new CameraFragmentResultAdapter() {
                                @Override
                                public void onVideoRecorded(String filePath) {
                                    Toast.makeText(getBaseContext(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPhotoTaken(byte[] bytes, String filePath) {
                                    transparentEffectImg.setVisibility(View.VISIBLE);
                                    effectLayout.setVisibility(View.VISIBLE);
                                    //  opacityBar.setVisibility(View.VISIBLE);
                                    recordButton.setVisibility(View.GONE);
                                    flashSwitchCameraView.setVisibility(View.VISIBLE);
                                    transparentEffectImg.setAlpha(0.5f);
                                    //   transparentEffectImg.setImageBitmap(getBitmapFromPath(filePath));
                                    Bitmap rootBitmap = scaleDown(getBitmapFromPath(filePath), 1000, true);
                                    Bitmap bitmap = rotateBitmap(rootBitmap, 90);
                                    transparentEffectImg.setImageBitmap(bitmap);

                                    /** To DO*/
                                    Toast.makeText(getBaseContext(), "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();
                                }
                            },
                            "/storage/self/primary",
                            "photo0");
        }
    }

    @OnClick(R.id.settings_view)
    public void onSettingsClicked() {
        Log.e("settings_view", "true++++");

        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.openSettingDialog();
        }
    }

    @OnClick(R.id.photo_video_camera_switcher)
    public void onMediaActionSwitchClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.switchActionPhotoVideo();
        }
    }

    @OnClick(R.id.addCameraButton)
    public void onAddCameraClicked() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            addCamera();
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void addCamera() {
        addCameraButton.setVisibility(View.GONE);
        cameraLayout.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR).build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();

        if (cameraFragment != null) {


            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitchView.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitchView.displayFrontCamera();
                }

//                @Override
//                public void onFlashAuto() {
//                    flashSwitchView.displayFlashAuto();
//                }

                @Override
                public void onFlashOn() {
                    flashSwitchView.displayFlashOn();
                }

//                @Override
//                public void onFlashOff() {
//                    flashSwitchView.displayFlashOff();
//                }

                @Override
                public void onCameraSetupForPhoto() {
                    mediaActionSwitchView.displayActionWillSwitchVideo();

                    recordButton.displayPhotoState();
                    flashSwitchView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCameraSetupForVideo() {
                    mediaActionSwitchView.displayActionWillSwitchPhoto();

                    recordButton.displayVideoRecordStateReady();
                    flashSwitchView.setVisibility(View.GONE);
                }

                @Override
                public void shouldRotateControls(int degrees) {
                    ViewCompat.setRotation(cameraSwitchView, degrees);
                    ViewCompat.setRotation(mediaActionSwitchView, degrees);
                    ViewCompat.setRotation(flashSwitchView, degrees);
                    ViewCompat.setRotation(recordDurationText, degrees);
                    ViewCompat.setRotation(recordSizeText, degrees);
                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                    recordButton.displayVideoRecordStateReady();
                }

                @Override
                public void onRecordStateVideoInProgress() {
                    recordButton.displayVideoRecordStateInProgress();
                }

                @Override
                public void onRecordStatePhoto() {
                    recordButton.displayPhotoState();
                }

                @Override
                public void onStopVideoRecord() {
                    recordSizeText.setVisibility(View.GONE);
                    //cameraSwitchView.setVisibility(View.VISIBLE);
                    settingsView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                }
            });

            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                    cameraSwitchView.setEnabled(false);
                    recordButton.setEnabled(false);
                    settingsView.setEnabled(false);
                    flashSwitchView.setEnabled(false);
                }

                @Override
                public void unLockControls() {
                    cameraSwitchView.setEnabled(true);
                    recordButton.setEnabled(true);
                    settingsView.setEnabled(true);
                    flashSwitchView.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    recordButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                    mediaActionSwitchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });

            cameraFragment.setTextListener(new CameraFragmentVideoRecordTextAdapter() {
                @Override
                public void setRecordSizeText(long size, String text) {
                    recordSizeText.setText(text);
                }

                @Override
                public void setRecordSizeTextVisible(boolean visible) {
                    recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                @Override
                public void setRecordDurationText(String text) {
                    recordDurationText.setText(text);
                }

                @Override
                public void setRecordDurationTextVisible(boolean visible) {
                    recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActvityReasa", "true +++");
        Log.e("resultCode", resultCode + "+++");

        switch (resultCode) {
            case -1:

                Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);// Galery


                if (bitmap != null) {
                    menu.close(true);
                    startCancelLayout.setVisibility(View.VISIBLE);
                    transparentEffectImg.setVisibility(View.VISIBLE);
                    effectLayout.setVisibility(View.VISIBLE);
                    //   opacityBar.setVisibility(View.VISIBLE);
                    transparentEffectImg.setAlpha(0.5f);
                    transparentEffectImg.setImageBitmap(bitmap);
                    Log.e("bitmap", bitmap.getGenerationId() + "+++");
                    Log.e("bitmap", "notNull+++");


                }

                break;

            case 10:

                InputStream is = null;
                try {
                    is = getAssets().open(SharedPreferenceHelper.getSharedPreferenceString(this, ConstantValues.CURRANT_PATH, ""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                menu.close(true);
                startCancelLayout.setVisibility(View.VISIBLE);
                //  opacityBar.setVisibility(View.VISIBLE);
                transparentEffectImg.setVisibility(View.VISIBLE);
                effectLayout.setVisibility(View.VISIBLE);
                int width = (int) (widthDisplay * 0.2);
                int height = (int) (heightDisolay * 0.2);

                /*Picasso.with(this)
                        .load("file:///android_asset/"+SharedPreferenceHelper.getSharedPreferenceString(this,ConstantValues.CURRANT_PATH,""))
                        .resize(widthDisplay,heightDisolay)
                        .into(transparentEffectImg);*/

                Glide.with(this)
                        .load("file:///android_asset/" + SharedPreferenceHelper.getSharedPreferenceString(this, ConstantValues.CURRANT_PATH, ""))
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Bitmap bitmap1 = resizeBitmapForScreen(resource);
                                transparentEffectImg.setImageBitmap(bitmap1);
                                transparentEffectImg.setScaleType(ImageView.ScaleType.MATRIX);
                            }
                        });

                // Bitmap bitmapAlbum = BitmapFactory.decodeStream(is);

//                transparentEffectImg.setImageBitmap(bitmapAlbum);
                transparentEffectImg.setAlpha(0.5f);


                break;
        }


    }

    Bitmap resizeBitmapForScreen(Bitmap bitmap) {

        Log.e("RRRR", "RRRRR");
        if (bitmap.getHeight() >= UTILUS.getDisplayHeight(this)) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (UTILUS.getDisplayWidth(this) * 0.7),
                    (int) (UTILUS.getDisplayHeight(this) * 0.7), true);
            return resizeBitmapForScreen(bitmap);
        } else return bitmap;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!clickStart) {

            ImageView view = (ImageView) v;
            view.setScaleType(ImageView.ScaleType.MATRIX);
            float scale;
            dumpEvent(event);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: //first finger down only
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    Log.e("mode=Drag", "mode=DRAG");
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    Log.e("mode=NONE", "mode=NONE");
                    break;


                case MotionEvent.ACTION_MOVE:


//                if(clickStart){
//
//
//                    float getX = event.getX();
//
//                    float alpha = getX/widthDisplay;
//
//
////                    float alpha = event.getX()/4;
////                    transparentEffectImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
////                    transparentEffectImg.setAlpha(alpha);
//
//                    Log.e("alpha", alpha + "++++");
//                    Log.e("getX", getX + "++++");
//                    Log.e("widthDisplay", widthDisplay + "++++");
//                } else {

                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);


                        //  Log.e("moveing",event.getX() + " x " + start.x + " x " + event.getY() + " x " + start.y);

                    } else if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist = spacing(event);
                        matrix.set(savedMatrix);
                        if (newDist > 10f) {
                            scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            float r = newRot - d;
                            matrix.postRotate(r, view.getMeasuredWidth() / 2,
                                    view.getMeasuredHeight() / 2);
                        }
                    }


                    break;

            }
            view.setImageMatrix(matrix);
            return true;

        } else {
            Log.e("Action", "Move" + event.getAction());


            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float alpha = event.getX() / widthDisplay;
                progValue(alpha);
            } if(event.getAction() == MotionEvent.ACTION_UP){
                opacityBar.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
            }

            return true;
            //v.animate().alpha(alpha);
        }


    }


    private void progValue(float x) {
        opacityBar.setVisibility(View.VISIBLE);
        seekbarLayout.setVisibility(View.VISIBLE);
        opacityBar.setProgress((int) (x * 255));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.ready_relativeLayout:
                menu.open(true);
                transparentEffectImg.setVisibility(View.GONE);
                effectLayout.setVisibility(View.GONE);
                readyLayout.setVisibility(View.GONE);

                break;

            case R.id.cancel_relativeLayout:
                readyLayout.setVisibility(View.GONE);
                menu.open(true);
                clickStart = false;
                Log.e("cancel", "cancel true +++");
                recordButton.setVisibility(View.VISIBLE);
                flashSwitchCameraView.setVisibility(View.VISIBLE);
                startCancelLayout.setVisibility(View.GONE);
                transparentEffectImg.setVisibility(View.GONE);
                effectLayout.setVisibility(View.GONE);
                opacityBar.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                break;

            case R.id.my_album_art_fab:
                readyLayout.setVisibility(View.GONE);
                startCancelLayout.setVisibility(View.GONE);
                menu.close(true);
                clickStart = false;
                opacityBar.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                recordButton.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.GONE);
                ImagePicker.pickImage(this, "Select your image:");
                break;

            case R.id.start_relativeLayout:
                readyLayout.setVisibility(View.VISIBLE);
                clickStart = true;
                Log.e("start", "start true +++");
                menu.close(true);
              //  opacityBar.setVisibility(View.VISIBLE);
//                transparentEffectImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                recordButton.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.GONE);
                startCancelLayout.setVisibility(View.GONE);


                //run seekbar



                //




                break;

            case R.id.camera_fab:
                readyLayout.setVisibility(View.GONE);
                clickStart = false;
                menu.close(true);
                recordButton.setVisibility(View.VISIBLE);
                startCancelLayout.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.VISIBLE);
                transparentEffectImg.setVisibility(View.GONE);
                effectLayout.setVisibility(View.GONE);
                opacityBar.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                break;

            case R.id.my_settings_fab:
                readyLayout.setVisibility(View.GONE);
                startCancelLayout.setVisibility(View.GONE);
                recordButton.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.GONE);
                menu.close(true);
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;

            case R.id.my_album_fab:
                readyLayout.setVisibility(View.GONE);
                startCancelLayout.setVisibility(View.GONE);
                menu.close(true);
                clickStart = false;
                opacityBar.setVisibility(View.GONE);
                seekbarLayout.setVisibility(View.GONE);
                recordButton.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.GONE);
                startActivityForResult(new Intent(MainActivity.this, MyAlbumActivity.class), 1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }

    }

    private void setOnClick() {
        readyLayout.setOnClickListener(this);
        effectLayout.setOnClickListener(this);
        settingsFab.setOnClickListener(this);
        photoFab.setOnClickListener(this);
        albumFab.setOnClickListener(this);
        myAlbumArtFab.setOnClickListener(this);
        menu.setOnClickListener(this);
        startRelativeLayout.setOnClickListener(this);
        cancelRelativeLayout.setOnClickListener(this);
    }


    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);

    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);

    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");

        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())

                sb.append(";");
        }

        sb.append("]");
        Log.e("sb string", sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    Bitmap getBitmapFromPath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
