package com.example.goku.using.service;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.goku.using.activity.LojaActivity;
import com.example.goku.using.prefs.PreferencesManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class NotificationIdService extends FirebaseInstanceIdService {

	private final String TAG = "NotificationIdService";

	@Override
	public void onTokenRefresh() {
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);

		Log.d(TAG, "onTokenRefresh: " + PreferencesManager.getString(this, "token"));
		if(PreferencesManager.getString(this, "token").isEmpty()) {
			PreferencesManager.setString(NotificationIdService.this, "token", refreshedToken);
		}else {
			sendRegistrationToServer(refreshedToken);
			PreferencesManager.setString(this, "token", refreshedToken);
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
					String url = PreferencesManager.getString(NotificationIdService.this, "URL") + "registerToken/";
					Gson g = new Gson();
					OkHttpClient client = new OkHttpClient();
					Request.Builder builder = new Request.Builder();
					builder.url(url);

					List<String> list = new ArrayList<>();

					list.add(token);
					list.add(PreferencesManager.getString(NotificationIdService.this, "cduser"));

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
				//Toast.makeText(NotificationIdService.this, s, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onPostExecute: " + s);
				super.onPostExecute(s);
			}
		};
		task.execute();

	}
}
