package com.github.florent37.camerafragment.sample;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/**
 * Created by hovsep on 3/31/17.
 */

public class UTILUS {

    public static int getDisplayHeight(Activity context) {

        Point size = new Point();
        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        }
        int y = size.y;
        return y;
    }

    public static int getDisplayWidth(Activity context) {

        Point size = new Point();
        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        }
        int x = size.x;
        return x;
    }
}
