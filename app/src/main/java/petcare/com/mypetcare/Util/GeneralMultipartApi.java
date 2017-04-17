package petcare.com.mypetcare.Util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KS on 2017-04-02.
 */

public class GeneralMultipartApi extends AsyncTask<Map<String, String>, Void, String> {
    private static final int CONNECTION_TIMEOUT = 2500;
    private static Global global = null;
    private static OkHttpClient client;
    private static Gson gson;
    private static JSONObject jsonInput;
    private static final MediaType MEDIA_TYPE_IMG = MediaType.parse("image/*");

    public static void setGlobal(Global global1) {
        global = global1;
    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        if (params.length != 2 || params[0] == null || params[1] == null) {
            return null;
        }

        Map<String, String> headerParam = params[0];
        Map<String, String> bodyParam = params[1];
        String email = global.getEmail();
        String url = MapUtils.getString(headerParam, "url");
        String serviceName = MapUtils.getString(headerParam, "serviceName");
        String filePath = MapUtils.getString(bodyParam, "path");
        String[] split = StringUtils.split(filePath, ".");
        String extension;

        if (split.length > 0) {
            extension = split[split.length - 1];
        } else {
            extension = "jpg";
        }

        if (StringUtils.isAnyEmpty(email, url, serviceName)) {
            return null;
        }

        client = new OkHttpClient();
        RequestBody rb = RequestBody.create(null, jsonInput.toString());
        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "1")
                .addFormDataPart("USER_EMAIL", email)
                .addFormDataPart("USER_NAME", StringUtils.EMPTY)
                .addFormDataPart("filename", extension, RequestBody.create(MEDIA_TYPE_IMG, new File(filePath))).build();

        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Authorization", global.getToken())
                .addHeader("SERVICE_NAME", serviceName)
                .build();

        Log.d("header", request.headers().toString());
        Log.d("body", request.toString());

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.d("response", result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setContext(Global global) {
        this.global = global;
    }
}
