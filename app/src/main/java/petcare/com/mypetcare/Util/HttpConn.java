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
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonObject;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import petcare.com.mypetcare.Model.HttpResultVO;

import static android.content.Context.MODE_PRIVATE;

public class HttpConn extends AsyncTask<Object, Void, HttpResultVO> {
    private static final int CONNECTION_TIMEOUT = 2500;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static Global global = null;
    private boolean isToken = false;

    public void setContext(Global global) {
        this.global = global;
    }

    @Override
    protected HttpResultVO doInBackground(Object... params) {
        if (params.length != 5) {
            return null;
        }

        if (!(params[0] instanceof String) || !(params[1] instanceof String) || !(params[2] instanceof String) || !(params[3] instanceof Map) || !(params[4] instanceof String)) {
            return null;
        }

        if (global == null) {
            return null;
        }

//        Boolean calling = MapUtils.getBoolean(global.getMap(), "token_api_calling");
//        if (calling) {
//            return null;
//        }
//        global.set("token_api_calling", true);

        String token = String.valueOf(params[4]);
        String contentType = String.valueOf(params[0]);
        String urlStr = String.valueOf(params[1]);
        String serviceName = String.valueOf(params[2]);
        Map<String, Object> paramMap = (Map<String, Object>) params[3];

        if (StringUtils.isAnyBlank(contentType, urlStr) || MapUtils.isEmpty(paramMap)) {
            return null;
        }

        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        GenericUrl url = new GenericUrl(urlStr);
        HttpRequest request;

        try {
            HttpContent content = new JsonHttpContent(new JacksonFactory(), paramMap);
            request = requestFactory.buildPostRequest(url, content);
            request.getHeaders().setContentType(contentType);

            if (StringUtils.isBlank(token)) {
                isToken = true;
            } else {
                request.getHeaders().setAuthorization(token);
            }

            if (StringUtils.isNotBlank(serviceName)) {
                request.getHeaders().set("SERVICE_NAME", serviceName);
            }

            request.setConnectTimeout(CONNECTION_TIMEOUT);
//            Log.d("http call auth: ", request.getHeaders().getAuthorization());
            HttpResultVO authResult = request.execute().parseAs(HttpResultVO.class);

            return authResult;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(HttpResultVO result) {
        super.onPostExecute(result);

        if (isToken && result != null && global != null) {
            try {
                String token = MapUtils.getString((Map) result.getData().get(0), "TOKEN");
                global.set("token", token);

                SharedPreferences pref = global.getSharedPreferences("local_auth", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("token", token);
                edit.putLong("auth_date", Calendar.getInstance().getTimeInMillis());
                edit.commit();
                Log.d("token", token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            global.set("token_api_calling", false);
        }
    }
}