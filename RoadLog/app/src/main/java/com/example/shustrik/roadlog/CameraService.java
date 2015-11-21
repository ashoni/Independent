package com.example.shustrik.roadlog;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraService extends IntentService {
    private Camera camera;
    private Camera.Size previewSize;
    private Context context;

    public CameraService() {
        super("RoadLogCamera");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    private void takePhoto() {
        Log.w("ANNA", "Take photo!");
        int l = 300;
        final CameraPreview cameraPreview = CameraPreview.getInstance(l, l, context);
        cameraPreview.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.w("ANNA", "Surface created");

                try {
                    camera.setPreviewDisplay(holder);

                    final int orient = CameraUtils.getScreenOrientation(context);
                    Log.w("ANNA", "orientation = " + orient);
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    camera.setParameters(parameters);
                    if (orient == 1) {
                        camera.setDisplayOrientation(90);
                    }

                    camera.startPreview();


                    camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] bytes, Camera camera) {
                            Log.w("ANNA", "Waiting (Motorola Special!)");
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {

                            }
                            camera.autoFocus(new Camera.AutoFocusCallback() {
                                @Override
                                public void onAutoFocus(boolean b, Camera camera) {
                                    if (b) {
                                        camera.takePicture(null, null, new PictureSaveCallback(true,
                                                orient, cameraPreview, "RoadLog", context));
                                    }
                                }
                            });
                        }
                    });


//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//
//                    }
//
//                    camera.autoFocus(new Camera.AutoFocusCallback() {
//                        @Override
//                        public void onAutoFocus(boolean b, Camera camera) {
//                            Log.w("ANNA", "autofocused? " + b);
//                            if (b) {
//                                camera.takePicture(null, null, new PictureSaveCallback(true, orient,
//                                        cameraPreview, "RoadLog", context));
//                            }
//                        }
//                    });


                } catch (IOException e) {
                    Log.w("ANNA", "Exception on setPreviewDisplay");
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w("ANNA", "On Start!");
        camera = CameraUtils.getBackCameraInstance();
        previewSize = CameraUtils.getOptimalPreviewSize(camera, 300);
        context = getApplicationContext();
        takePhoto();
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("ANNA", "On receive: " + System.currentTimeMillis());
            Intent sendIntent = new Intent(context, CameraService.class);
//            sendIntent.putExtra(SunshineService.LOCATION_QUERY_EXTRA, intent.getStringExtra(SunshineService.LOCATION_QUERY_EXTRA));
            context.startService(sendIntent);
        }
    }
}
