package com.example.shustrik.roadlog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 * <p>
 * Отслеживать изменения shared preferences
 */
public class MainActivityFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int REQUEST_CODE = 0;
    private static final long FIRST_SLEEP = 5000;

    private AlarmManager am;
    private PendingIntent cameraIntent;

    private boolean isActivated = false;


    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        View ctrlButton = rootView.findViewById(R.id.ctrl_button);
        ctrlButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("ANNA", "Big Red Button pressed");
                        if (isActivated) {
                            stopTakingPictures();
                        } else {
                            startTakingPictures();
                        }
                    }
                }
        );

        boolean cameraAvailable = Utility.checkCameraExists(getActivity());
        ctrlButton.setEnabled(cameraAvailable);
        if (!cameraAvailable) {
            Toast.makeText(getActivity(), "No camera on this device", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    public void stopTakingPictures() {
        Log.w("ANNA", "Stop taking pictures");
        if (cameraIntent != null) {
            am.cancel(cameraIntent);
        }
    }

    public void startTakingPictures() {
        Log.w("ANNA", "Start taking pictures");
        if (cameraIntent == null) {
            createCameraIntent();
        }
        Log.w("ANNA", FIRST_SLEEP + ":-:" + Utility.getPicturesPeriod(getActivity()) + ":-:" + cameraIntent);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, FIRST_SLEEP,
                Utility.getPicturesPeriod(getActivity()), cameraIntent);
    }

    private void createCameraIntent() {
        Intent alarmIntent = new Intent(getActivity(), CameraService.AlarmReceiver.class);
        alarmIntent.putExtra("", 0);
        Log.w("ANNA", "Create camerA intent");
        cameraIntent = PendingIntent.getBroadcast(getActivity(), REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (getString(R.string.picture_interval).equals(s)) {
            createCameraIntent();
        }
    }

    @Override
    public void onDestroy() {
        stopTakingPictures();
        super.onDestroy();
    }
}
