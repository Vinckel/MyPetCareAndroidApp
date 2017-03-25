package petcare.com.mypetcare.Util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Model.AuthResultVO;

import static android.content.Context.MODE_PRIVATE;

public class AuthHttpConn extends AsyncTask<String, Void, String> {
    private static final int CONNECTION_TIMEOUT = 2500;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static Global global = null;

    public void setContext(Global global) {
        this.global = global;
    }

    @Override
    protected String doInBackground(String... params) {
        if (global == null) {
            return null;
        }

        Boolean calling = MapUtils.getBoolean(global.getMap(), "calling");
        if (calling) {
            return null;
        }

        global.set("calling", true);

        List<String> paramList = Arrays.asList(params);

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        GenericUrl url = new GenericUrl("http://220.73.175.100:8080/MPMS/mob/auth.service");

        HttpRequest request = null;

        try {
            Map<String, String> json = new HashMap<>();
            json.put("USER_EMAIL", "test@test.com");

            HttpContent content = new JsonHttpContent(new JacksonFactory(), json);
            request = requestFactory.buildPostRequest(url, content);
            request.getHeaders().setContentType("application/json");
            request.setConnectTimeout(CONNECTION_TIMEOUT);
            AuthResultVO authResult = request.execute().parseAs(AuthResultVO.class);
            String token = MapUtils.getString((Map) authResult.getData().get(0), "TOKEN");

            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null && global != null) {
            global.set("token", result);

            SharedPreferences pref = global.getSharedPreferences("local_auth", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("token", result);
            edit.putLong("auth_date", Calendar.getInstance().getTimeInMillis());
            edit.commit();
            Log.d("token", result);
        }

        global.set("calling", false);
    }
}