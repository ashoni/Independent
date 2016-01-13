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

    private static final int OPEN_CAMERA_ATTEMPTS = 3;
    private static final int TAKE_PICTURE_ATTEMPTS = 3;

    public CameraService() {
        super("RoadLogCamera");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    private void takePhoto(final String albumName, final int focusMode, final int pictureAmount) {
        Log.w("ANNA", "Take photo!");
        int l = 300;
        final CameraPreview cameraPreview = CameraPreview.getInstance(previewSize.width,
                previewSize.height, context);
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
                    if (focusMode == 1) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    }
                    camera.setParameters(parameters);
                    if (orient == 1) {
                        camera.setDisplayOrientation(90);
                    }

                    takePicture(0, pictureAmount, 0, orient, cameraPreview, albumName);

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

    private void takePicture(final int n, final int maxN, final int err, final int orient,
                             final CameraPreview cameraPreview, final String albumName) {
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
                                    orient, cameraPreview, albumName, context));
                            if (n + 1 < maxN) {
                                sleep(1000);
                                takePicture(n + 1, maxN, err, orient, cameraPreview, albumName);
                            } else {
                                camera.release();
                            }
                        } else {
                            if (err + 1 < TAKE_PICTURE_ATTEMPTS) {
                                sleep(1000);
                                takePicture(n, maxN, err + 1, orient, cameraPreview, albumName);
                            } else {
                                camera.release();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w("ANNA", "On Start!");

        camera = null;
        int i = 0;
        while (camera == null && i < OPEN_CAMERA_ATTEMPTS) {
            if (intent.getBooleanExtra("pref_camera", true)) {
                camera = CameraUtils.getBackCameraInstance();
            } else {
                camera = CameraUtils.getFrontCameraInstance();
            }
            if (camera == null) {
                sleep(5000);
            }
            i ++;
        }

        if (camera == null) {
            return;
        }

        previewSize = CameraUtils.getOptimalPreviewSize(camera, 300);
        context = getApplicationContext();
        takePhoto(intent.getStringExtra("album_name"), intent.getIntExtra("pref_focus", 0),
                intent.getIntExtra("pref_picture_amount", 1));
    }

    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {

        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("ANNA", "On receive: " + System.currentTimeMillis());
            Intent sendIntent = new Intent(context, CameraService.class);
            sendIntent.putExtra("pref_camera", intent.getBooleanExtra("pref_camera", true));
            sendIntent.putExtra("pref_focus", intent.getIntExtra("pref_focus", 0));
            sendIntent.putExtra("album_name", intent.getStringExtra("album_name"));
            sendIntent.putExtra("pref_picture_amount", intent.getIntExtra("pref_picture_amount", 1));
            context.startService(sendIntent);
        }
    }
}
