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
import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import petcare.com.mypetcare.Model.HttpResultVO;
import petcare.com.mypetcare.Model.TokenVO;

import static android.content.Context.MODE_PRIVATE;

public class TokenApi extends AsyncTask<String, Void, String> {
    private static final int CONNECTION_TIMEOUT = 2500;
    private static Global global = null;
    private static OkHttpClient client;
    private static Gson gson;
    private static JSONObject jsonInput;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void setContext(Global global) {
        this.global = global;
    }

    @Override
    protected String doInBackground(String... emails) {
        global.set("token_api_calling", true);

        if (emails.length != 1 || StringUtils.isEmpty(emails[0])) {
            return null;
        }

        client = new OkHttpClient();

        String email = emails[0];
        jsonInput = new JSONObject();

        try {
            jsonInput.put("USER_EMAIL", email);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        String urlStr = "http://220.73.175.100:8080/MPMS/mob/auth.service";
        RequestBody rb = RequestBody.create(JSON, jsonInput.toString());
        Request request = new Request.Builder()
                .url(urlStr)
                .post(rb)
                .build();

        try {
            Response response = client.newCall(request).execute();
            gson = new Gson();
            String result = response.body().string();
            TokenVO tokenVO = gson.fromJson(result, TokenVO.class);

            if (tokenVO.getResultCode() != 0) {
                return null;
            }

            String token = tokenVO.getToken();

            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        if (StringUtils.isNotEmpty(token) && global != null) {
            try {
                global.set("token", token);

                SharedPreferences pref = global.getSharedPreferences("local_auth", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("token", token);
                edit.putLong("auth_date", Calendar.getInstance().getTimeInMillis());
                edit.apply();
                Log.d("token", token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            global.set("token_api_calling", false);
        }
    }
}