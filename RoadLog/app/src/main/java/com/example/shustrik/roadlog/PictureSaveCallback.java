package com.example.shustrik.roadlog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class PictureSaveCallback implements Camera.PictureCallback {
    private boolean needCameraRelease;
    private CameraPreview preview;
    private Context context;
    private String albumName;
    private int orientation;

    public static final String ACTION = "com.example.shustrik.roadlog.CAPTURED";

    public PictureSaveCallback(boolean needCameraRelease, int orientation, CameraPreview preview,
                               String albumName, Context context) {
        this.needCameraRelease = needCameraRelease;
        this.preview = preview;
        this.albumName = albumName;
        this.context = context;
        this.orientation = orientation;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

        Log.w("ANNA", "W=" + bm.getWidth() + ", H=" + bm.getHeight());

        if (needCameraRelease) {
            Log.w("ANNA", "Release camera: " + System.currentTimeMillis());
            camera.release();
            preview.removePreview();
        }

        File pictureFile = FileUtils.getOutputMediaFile(albumName);
        if (pictureFile == null) {
            Log.w("ANNA", "File is null");
            return;
        }
            if (FileUtils.saveToFile(pictureFile, data)) {
                if (orientation == 1) {
                    try {
                        ExifInterface exif = new ExifInterface(pictureFile.getPath());
                        exif.setAttribute(ExifInterface.TAG_ORIENTATION,
                                String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
                        exif.saveAttributes();
                    } catch (IOException e) {
                        Log.w("ANNA", "Can't set attributes");
                    }
                }
                //broadcast: send notification to activity (at least Hey! One more!)
                FileUtils.galleryAddPic(pictureFile.getPath(), context);
                Intent intent = new Intent();
                intent.setAction(ACTION);
                context.sendBroadcast(intent);
            }
    }
}
