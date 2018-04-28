package com.example.goku.using.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.activity.MenuActivity;
import com.example.goku.using.prefs.PreferencesManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Goku on 18/08/2017.
 */

public class NotificationService extends FirebaseMessagingService {
	private final String TAG = "FIREBASE SERVICE";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);


		Log.d(TAG, "From: " + remoteMessage.getFrom());
		Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

		Intent i = new Intent(this, MenuActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{i},PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.u_cortado)
				.setContentTitle(remoteMessage.getNotification().getTitle())
				.setContentText(remoteMessage.getNotification().getBody())
				.setContentIntent(pendingIntent);

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(0, mBuilder.build());
		if (PreferencesManager.getString(this, "token") != null){
			sendRegistrationToServer(PreferencesManager.getString(this, "token"));
		}
	}

	private void sendRegistrationToServer(final String token){
		AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			@Override
			protected String doInBackground(String... params) {
				try{
					
					String url = PreferencesManager.getString(NotificationService.this, "URL") + "registerToken/";
					Gson g = new Gson();
					OkHttpClient client = new OkHttpClient();
					Request.Builder builder = new Request.Builder();
					builder.url(url);

					List<String> list = new ArrayList<>();

					list.add(token);
					list.add(PreferencesManager.getString(NotificationService.this, "cduser"));

					MediaType mediaType =
							MediaType.parse("application/json; charset=utf-8");
					RequestBody body = RequestBody.create(mediaType, g.toJson(list));

					builder.post(body);

					Request request = builder.build();
					okhttp3.Response response = client.newCall(request).execute();

					String jsonDeResposta = response.body().string();
					return jsonDeResposta;
				} catch (IOException e) {
					e.printStackTrace();
					return e.toString();
				}
			}

			@Override
			protected void onPostExecute(String s) {
				Log.d(TAG, "onPostExecute: " +s );
				super.onPostExecute(s);
			}
		};
		task.execute();

	}
}
