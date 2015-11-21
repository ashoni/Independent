package com.example.shustrik.roadlog;


import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.List;


@SuppressWarnings("deprecation")
public class CameraUtils {
    /** Open any camera */
    public static Camera getCameraInstance() {
        return getCameraInstance(-1);
    }

    /** Open this camera */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            if(cameraId == -1) {
                c = Camera.open(); // attempt to get a Camera instance
            } else {
                c = Camera.open(cameraId); // attempt to get a Camera instance
            }
        }
        catch (Exception e) {
            Log.w("ANNA", "Camera not available");
        }
        return c; // returns null if camera is unavailable
    }

    public static Camera getBackCameraInstance() {
        return getCameraWithFacing(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public static Camera getFrontCameraInstance() {
        return getCameraWithFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public static Camera getCameraWithFacing(int facing) {
        try {
            for (int i = 0; i < Camera.getNumberOfCameras() - 1; i++) {
                Camera c = getCameraInstance(i);
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == facing) {
                    return c;
                }
            }
        } catch (Exception e) {
            Log.w("ANNA", "Camera not available");
        }
        return null;
    }

    public static Camera.Size getOptimalPreviewSize(Camera camera, int maxLen) {
        List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        Camera.Size optimal = sizes.get(0);
        int minDiff = Math.abs(2*maxLen - optimal.width - optimal.height);
        for (Camera.Size size : sizes) {
            int diff = Math.abs(2*maxLen - optimal.width - optimal.height);
            if (diff < minDiff) {
                minDiff = diff;
                optimal = size;
            }
        }
        return optimal;
    }

    public static int getScreenOrientation(Context context)
    {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation;
        if(display.getWidth()==display.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            orientation = display.getWidth() < display.getHeight() ?
                    Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
        }
        return orientation;
    }

//    this.matrix.reset();
//    this.matrix.setTranslate(this.floatXpos, this.floatYpos);
//    this.matrix.postRotate((float)this.direction, this.getCenterX(), this.getCenterY());

//    bitmap = (Bitmap) extras.get("data");
//    ExifInterface ei = new ExifInterface(photoPath);
//int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//    switch(orientation) {
//        case ExifInterface.ORIENTATION_ROTATE_90:
//            rotateImage(bitmap, 90);
//            break;
//        case ExifInterface.ORIENTATION_ROTATE_180:
//            rotateImage(bitmap, 180);
//            break;
//        // etc.
//    }

//    private static Bitmap rotateImageIfRequired(Context context,Bitmap img, Uri selectedImage) {
//
//        // Detect rotation
//        int rotation=getRotation(context, selectedImage);
//        if(rotation!=0){
//            Matrix matrix = new Matrix();
//            matrix.postRotate(rotation);
//            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
//            img.recycle();
//            return rotatedImg;
//        }else{
//            return img;
//        }
//    }

//    private static int getRotation(Context context,Uri selectedImage) {
//        int rotation =0;
//        ContentResolver content = context.getContentResolver();
//
//
//        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[] { "orientation", "date_added" },null, null,"date_added desc");
//
//        if (mediaCursor != null && mediaCursor.getCount() !=0 ) {
//            while(mediaCursor.moveToNext()){
//                rotation = mediaCursor.getInt(0);
//                break;
//            }
//        }
//        mediaCursor.close();
//        return rotation;
//    }

//    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, config);
//    Canvas canvas = new Canvas(targetBitmap);
//    Matrix matrix = new Matrix();
//    matrix.setRotate(mRotation,source.getWidth()/2,source.getHeight()/2);
//    canvas.drawBitmap(source, matrix, new Paint());

//    public static Point getScreenResolution(Context context) {
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point screenResolution = new Point();
//        if (android.os.Build.VERSION.SDK_INT >= 13) {
//            display.getSize(screenResolution);
//        } else {
//            screenResolution.set(display.getWidth(), display.getHeight());
//        }
//
//        return screenResolution;
//    }
//
//    public static boolean isFlashSupported(Camera camera) {
//        /* Credits: Top answer at http://stackoverflow.com/a/19599365/868173 */
//        if (camera != null) {
//            Camera.Parameters parameters = camera.getParameters();
//
//            if (parameters.getFlashMode() == null) {
//                return false;
//            }
//
//            List<String> supportedFlashModes = parameters.getSupportedFlashModes();
//            if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
//                return false;
//            }
//        } else {
//            return false;
//        }
//
//        return true;
//    }
}
