package petcare.com.mypetcare.Util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
        } catch (Exception e) {
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