package com.indo.app.mygcmnetworkmanagement;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by indo on 07/01/18.
 */

public class SchedulerService extends GcmTaskService {

    public static  final String TAG = "GetWeater";
    private final String APP_ID ="c963ea1922f176014be1b3b4edadd5a0";
    private final String CITY = "Jakarta";
    public static  String TAG_TASK_WHEATHER_LOG ="WeatherTask";


    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_WHEATHER_LOG)){
            getCurrentWeather();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getCurrentWeather() {
        Log.d("GetWeather", "Running");
        SyncHttpClient client = new SyncHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+CITY+"&apiid="+APP_ID;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responObject = new JSONObject(result);
                    String currentweather = responObject.getJSONArray("weather").getJSONObject(0).getString("main");
                    String description = responObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    double tempInKelvin = responObject.getJSONArray("main").getJSONObject(0).getDouble("temperature");
                    double temperature = tempInKelvin - 273;
                    String title = "CurrentWheater";
                    String message = currentweather+ ", "+ description+ "with"+ temperature+ " celcius";
                    int notifId = 100;
                    showNotification(getApplicationContext(), title, message, notifId);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("GeatWeather", "Failed");

            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask  mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }
    private void showNotification(Context context, String title, String message, int notifId){
        NotificationManager notificationManagerCompact = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        notificationManagerCompact.notify(notifId, builder.build());
    }
}
