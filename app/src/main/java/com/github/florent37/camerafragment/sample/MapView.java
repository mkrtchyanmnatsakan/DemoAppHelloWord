package com.github.florent37.camerafragment.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.flurgle.camerakit.CameraView;

/**
 * Created by mno on 3/20/17.
 */

public class MapView extends CameraView implements SurfaceHolder.Callback {


    Canvas canvas;
    private SurfaceHolder mSurfaceHolder;
    private int mFormat;
    private int mWidth;
    private int mHeight;
    private Bitmap scaled;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

//        getHolder().addCallback(this);
    }

    public void onDraw(Canvas canvas) {
        if (canvas != null && scaled != null) {
            Paint paint = new Paint();
            paint.setAlpha(42);
            this.canvas = canvas;

            canvas.drawBitmap(scaled, 0, 0, paint); // draw the background
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {


        Log.e("create", " create,,");
  /* Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.naruto);

        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);*/


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mSurfaceHolder = holder;
        this.mFormat = format;
        this.mWidth = width;
        this.mHeight = height;
        // TODO Callback method contents
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Callback method contents
    }

   /* public void setImage(Bitmap background) {
//
        scaled = background;
        if (canvas == null) {
            Log.e("gggg", "null");
        } else {
            Log.e("gggg", "gggggg");
            drawMyStuff(canvas, background);
            // mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

  *//*      float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);

        Paint paint = new Paint();
        paint.setAlpha(42);
        canvas.drawBitmap(scaled, 0, 0,paint );*//*


    }*/

    private void drawMyStuff(final Canvas canvas, Bitmap bitmap) {
       /* Random random = new Random();
        canvas.drawRGB(255, 128, 128);*/
        Paint paint = new Paint();
        paint.setAlpha(42);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();

    }
}
