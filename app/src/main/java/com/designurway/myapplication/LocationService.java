package com.designurway.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    static final int LOCATION_SERVICE_ID = 175;
    static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            // here we will get updated location
//            Toast.makeText(TrackOrderMapActivity.this, locationResult.getLastLocation().toString(), Toast.LENGTH_SHORT).show();
//            CurrentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

            // whenever we will get update tocation we will set the marker and we will setDriverMarker when mapp is ready
            // mMap is inatialised inside the onMap Ready method
//            if (mMap != null) {
//                updateDeliveryBoyLocation(String.valueOf(locationResult.getLastLocation().getLatitude()), String.valueOf(locationResult.getLastLocation().getLongitude()));
////                Toast.makeText(TrackOrderMapActivity.this, locationResult.getLastLocation().toString(), Toast.LENGTH_SHORT).show();
//
//                setDriverLocationMarker(locationResult.getLastLocation());
//                CurrentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
//                Log.d("DestinationLatLang", "" + CurrentLatLng);
//
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double Latitude = locationResult.getLastLocation().getLatitude();
                double Longitute = locationResult.getLastLocation().getLongitude();
                Log.d("LOCATIONMy", Latitude + "," + Longitute);

            }

        }
    };

    @SuppressLint("MissingPermission")
    private void startLocatinService() {
        String ChannnelId = "location_notification_service";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ChannnelId);


        builder.setContentTitle("Location service");
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(ChannnelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(ChannnelId, "Location Service"
                        , NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("this channel is  used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        startForeground(LOCATION_SERVICE_ID, builder.build());


    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(ACTION_START_LOCATION_SERVICE)) {
                    startLocatinService();
                } else if (action.equals(ACTION_START_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return START_STICKY;
    }
}


