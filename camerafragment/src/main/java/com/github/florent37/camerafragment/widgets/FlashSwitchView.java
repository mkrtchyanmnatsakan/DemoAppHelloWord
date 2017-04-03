package com.github.florent37.camerafragment.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageButton;

import static com.github.florent37.camerafragment.R.drawable;


/*
 * Created by memfis on 7/6/16.
 */
public class FlashSwitchView extends ImageButton {

    private Drawable flashOnDrawable;
    private Drawable flashOffDrawable;
   // private Drawable flashAutoDrawable;

    public FlashSwitchView(@NonNull Context context) {
        this(context, null);
    }

    public FlashSwitchView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        flashOnDrawable = ContextCompat.getDrawable(context, drawable.camera_flash);
        flashOffDrawable = ContextCompat.getDrawable(context, drawable.camera_flash);
     //  flashOffDrawable.setColorFilter(context.getColor(color.colorPrimaryDark));
      //  flashAutoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_flash_auto_white_24dp);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        displayFlashOn();
    }

    public void displayFlashOff() {
        setImageDrawable(flashOffDrawable);
    }

    public void displayFlashOn() {
        setImageDrawable(flashOnDrawable);
    }

    public void displayFlashAuto() {
       // setImageDrawable(flashAutoDrawable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                setAlpha(1f);
            } else {
                setAlpha(0.5f);
            }
        }
    }

}
