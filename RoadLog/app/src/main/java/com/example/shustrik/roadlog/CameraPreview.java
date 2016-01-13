package com.example.shustrik.roadlog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView {
    private Context context;
    private static final int WINDOW_X_POSITION = 200;
    private static final int WINDOW_Y_POSITION = 400;

    private CameraPreview(Context context) {
        super(context);
        this.context = context;
    }

    public static CameraPreview getInstance(int w, int h, Context context) {
        CameraPreview preview = new CameraPreview(context);

        WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                w,
                h,
                WINDOW_X_POSITION,
                WINDOW_Y_POSITION,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        //Don't set the preview visibility to GONE or INVISIBLE
        wm.addView(preview, params);

        preview.getHolder().setFormat(PixelFormat.TRANSPARENT);
        preview.setZOrderOnTop(true);

        return preview;
    }

    public void addCallback(SurfaceHolder.Callback callback) {
        getHolder().addCallback(callback);
    }

    public void removePreview() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.removeViewImmediate(this);
    }
}
