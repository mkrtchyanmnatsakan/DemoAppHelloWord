package com.github.florent37.camerafragment.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String FRAGMENT_TAG = "camera";
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int REQUEST_PREVIEW_CODE = 1001;
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

    //////////////////////////////////////////////

    private boolean isOpenFlesh = true;
    private SurfaceHolder surfaceHolder;
    private Bitmap alertBitmap;
    private Canvas canvas;
    ImageView transparentEffectImg;

    LinearLayout flashSwitchCameraView;
    LinearLayout startCancelLayout;

    TextView startTextView;
    TextView cancelTextView;

    RelativeLayout startRelativeLayout;
    RelativeLayout cancelRelativeLayout;

    SeekBar opacityBar;

    FloatingActionButton photoFab;
    FloatingActionButton albumFab;
    FloatingActionButton myAlbumArtFab;

    FloatingActionButton cameraFab;
    FloatingActionButton automaticRotationFab;
    FloatingActionButton startPaintFab;
    FloatingActionButton rotationBy5Fab;
    FloatingActionButton replayBy5Fab;
    FloatingActionButton plusFab;
    FloatingActionButton minusFab;
    FloatingActionButton fleshFab;
    FloatingActionMenu menu;
    private Bitmap dinoBMP;

    PhotoViewAttacher mAttacher;
    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
    private Toast mCurrentToast;

    private final Handler handler = new Handler();
    private boolean rotating = false;
    private int i = 127;


    //////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


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


        /////////////////////////////////


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        transparentEffectImg = (ImageView) findViewById(R.id.transparet_efect_imageView);


        dinoBMP = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_imag);

        Bitmap scaledBitmap = scaleDown(dinoBMP, 800, true);


//        narutoBMP.setWidth(200);
//        narutoBMP.setHeight(200);

        transparentEffectImg.setImageBitmap(scaledBitmap);

        transparentEffectImg.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        transparentEffectImg.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
        transparentEffectImg.setScaleType(ImageView.ScaleType.FIT_START);
        mAttacher = new PhotoViewAttacher(transparentEffectImg);
        // mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
        mAttacher.setOnSingleFlingListener(new SingleFlingListener());
        mAttacher.setScaleType(ImageView.ScaleType.CENTER);
        mAttacher.setZoomable(true);


        //mAttacher.setScaleLevels(10f,30f,80f);


        ImagePicker.setMinQuality(800, 600);
        photoFab = (FloatingActionButton) findViewById(R.id.camera_fab);
        photoFab.setOnClickListener(this);

        albumFab = (FloatingActionButton) findViewById(R.id.my_album_fab);
        albumFab.setOnClickListener(this);

        automaticRotationFab = (FloatingActionButton) findViewById(R.id.rotation_fab);
        automaticRotationFab.setOnTouchListener(this);

        rotationBy5Fab = (FloatingActionButton) findViewById(R.id.rotation_by_5_fab);
        rotationBy5Fab.setOnClickListener(this);

        replayBy5Fab = (FloatingActionButton) findViewById(R.id.replay_by_5_fab);
        replayBy5Fab.setOnClickListener(this);

        myAlbumArtFab = (FloatingActionButton) findViewById(R.id.my_album_art_fab);
        myAlbumArtFab.setOnClickListener(this);

//        plusFab = (FloatingActionButton) findViewById(R.id.plus_fab);
//        plusFab.setOnClickListener(this);

//        minusFab = (FloatingActionButton) findViewById(R.id.minus_fab);
//        minusFab.setOnClickListener(this);


        // countOfScans = db.getCountofPictures();
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.setOnClickListener(this);

//        startPaintFab = (FloatingActionButton) findViewById(R.id.start_paint_fab);
//        startPaintFab.setOnClickListener(this);
        //  galleryFab = (FloatingActionButton) findViewById(R.id.gallery_fab);

        flashSwitchCameraView = (LinearLayout) findViewById(R.id.flash_switch_camera_view);
        startCancelLayout = (LinearLayout) findViewById(R.id.start_cancel_layout);

//        startTextView = (TextView) findViewById(R.id.start_textView);
//        startTextView.setOnClickListener(this);

        startRelativeLayout = (RelativeLayout) findViewById(R.id.start_relativeLayout);
        startRelativeLayout.setOnClickListener(this);

//        cancelTextView = (TextView) findViewById(R.id.cancel_textView);
//        cancelTextView.setOnClickListener(this);

        cancelRelativeLayout = (RelativeLayout) findViewById(R.id.cancel_relativeLayout);
        cancelRelativeLayout.setOnClickListener(this);


        opacityBar = (SeekBar) findViewById(R.id.opacity);

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


        ////////////////////////////////////

        // flashSwitchView.displayFlashOff();


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

        flashSwitchCameraView.setVisibility(View.VISIBLE);
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
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
            cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultAdapter() {
                                                       @Override
                                                       public void onVideoRecorded(String filePath) {
                                                           Toast.makeText(getBaseContext(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
                                                       }

                                                       @Override
                                                       public void onPhotoTaken(byte[] bytes, String filePath) {

                                                           transparentEffectImg.setVisibility(View.VISIBLE);
                                                           opacityBar.setVisibility(View.VISIBLE);
                                                           recordButton.setVisibility(View.GONE);
                                                           flashSwitchCameraView.setVisibility(View.VISIBLE);


                                                           transparentEffectImg.setAlpha(0.5f);

                                                           //   transparentEffectImg.setImageBitmap(getBitmapFromPath(filePath));
                                                           transparentEffectImg.setImageBitmap(scaleDown(getBitmapFromPath(filePath), 800, true));
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
//        if (Build.VERSION.SDK_INT > 15) {
//            final String[] permissions = {
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE};
//
//            final List<String> permissionsToRequest = new ArrayList<>();
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                    permissionsToRequest.add(permission);
//                }
//            }
//            if (!permissionsToRequest.isEmpty()) {
//                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
//            } else addCamera();
//        } else {
//            addCamera();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR).build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();

        if (cameraFragment != null) {
            //cameraFragment.setResultListener(new CameraFragmentResultListener() {
            //    @Override
            //    public void onVideoRecorded(String filePath) {
            //        Intent intent = PreviewActivity.newIntentVideo(MainActivity.this, filePath);
            //        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            //    }
//
            //    @Override
            //    public void onPhotoTaken(byte[] bytes, String filePath) {
            //        Intent intent = PreviewActivity.newIntentPhoto(MainActivity.this, filePath);
            //        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            //    }
            //});

            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitchView.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitchView.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    flashSwitchView.displayFlashAuto();
                }

                @Override
                public void onFlashOn() {
                    flashSwitchView.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    flashSwitchView.displayFlashOff();
                }

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

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }


    //////////////////////////////////////


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


    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

            showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }

        @Override
        public void onOutsidePhotoTap() {
            showToast("You have a tap event on the place where out of the photo.");
        }
    }

    private void showToast(CharSequence text) {
        if (null != mCurrentToast) {
            mCurrentToast.cancel();
        }

        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }


    private class SingleFlingListener implements PhotoViewAttacher.OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (BuildConfig.DEBUG) {

//                transparentEffectImg.setMaxWidth((int) velocityX);
//                transparentEffectImg.setMaxHeight((int) velocityY);
//                mAttacher.setMaximumScale(velocityX);
                // Log.d("PhotoView", String.format(FLING_LOG_STRING, velocityX, velocityY));
            }
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rotating = false;
                toggleRotation();
                break;
            case MotionEvent.ACTION_UP:
                rotating = true;
                toggleRotation();

        }
        return false;
    }


    private void toggleRotation() {
        if (rotating) {
            handler.removeCallbacksAndMessages(null);
        } else {
            rotateLoop();
        }
        rotating = !rotating;
    }

    private void rotateLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAttacher.setRotationBy(1);
                rotateLoop();
            }
        }, 15);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cancel_relativeLayout:
                Log.e("cancel","cancel true +++");
                recordButton.setVisibility(View.VISIBLE);
                flashSwitchCameraView.setVisibility(View.VISIBLE);
                startCancelLayout.setVisibility(View.GONE);
                transparentEffectImg.setVisibility(View.GONE);
                opacityBar.setVisibility(View.GONE);


                break;

            case R.id.my_album_art_fab:
                startActivity(new Intent(MainActivity.this, MyAlbumActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);


                break;

            case R.id.start_relativeLayout:

                Log.e("start","start true +++");
                menu.close(true);
                recordButton.setVisibility(View.GONE);
                flashSwitchCameraView.setVisibility(View.GONE);
                startCancelLayout.setVisibility(View.GONE);

                break;


//            case R.id.start_paint_fab:
//                menu.close(true);
//                recordButton.setVisibility(View.GONE);
//                flashSwitchCameraView.setVisibility(View.GONE);
//                break;

            case R.id.camera_fab:
                menu.close(true);
                recordButton.setVisibility(View.VISIBLE);
                flashSwitchCameraView.setVisibility(View.VISIBLE);
                transparentEffectImg.setVisibility(View.GONE);
                opacityBar.setVisibility(View.GONE);


                break;

            case R.id.rotation_fab:
                toggleRotation();
                break;

            case R.id.rotation_by_5_fab:
                mAttacher.setRotationBy(-1);
                break;

            case R.id.replay_by_5_fab:
                mAttacher.setRotationBy(1);
                break;
            case R.id.my_album_fab:
                ImagePicker.pickImage(this, "Select your image:");
                break;

//            case R.id.minus_fab:
//
//                i = i - 10;
//
//                transparentEffectImg.setAlpha((float) i / 255);
//
//
////
////                AlphaAnimation alpha = new AlphaAnimation(-0.5F, -0.01F); // change values as you want
////                alpha.setDuration(0); // Make animation instant
////                alpha.setFillAfter(true); // Tell it to persist after the animation ends
////// And then on your imageview
////                transparentEffectImg.startAnimation(alpha);
////
////                mAttacher = new PhotoViewAttacher(transparentEffectImg);
//                break;
//
//            case R.id.plus_fab:
//
//                i = i + 10;
//
//                transparentEffectImg.setAlpha((float) i / 255);
//
//
////                AlphaAnimation alphaPlus = new AlphaAnimation(0.5F, 0.01F); // change values as you want
////                alphaPlus.setDuration(0); // Make animation instant
////                alphaPlus.setFillAfter(true); // Tell it to persist after the animation ends
////// And then on your imageview
////                transparentEffectImg.startAnimation(alphaPlus);
//                break;


        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {

        super.onPause();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActvityReasa", "true +++");
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);// Galery
        if (bitmap != null) {

            startCancelLayout.setVisibility(View.VISIBLE);

            transparentEffectImg.setVisibility(View.VISIBLE);
            opacityBar.setVisibility(View.VISIBLE);
            transparentEffectImg.setAlpha(0.5f);
            transparentEffectImg.setImageBitmap(bitmap);
            Log.e("bitmap", bitmap.getGenerationId() + "+++");
            Log.e("bitmap", "notNull+++");
            /*Bitmap transparentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.naruto);
            Paint paint = new Paint();
            paint.setAlpha(42);

            canvas.drawBitmap(bitmap, 0, 0, paint);


            currentImg.setImageBitmap(bitmap);*/


        }

    }

    Bitmap getBitmapFromPath(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }


    //////////////////////////////////


}
