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
import java.util.ArrayList;
import java.util.List;
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

public class GeneralMultipartApi extends AsyncTask<Map<String, String>, Void, List<String>> {
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
    protected List<String> doInBackground(Map<String, String>... params) {
        List<String> resultList = new ArrayList<>();
        if (params.length < 1 || params.length > 3) {
            return null;
        }

        Map<String, String> header = params[0];
        Map<String, String> body = params[1];
        Map<String, String> files = params[2];

        String email = global.getEmail();

        String url = MapUtils.getString(header, "url");
        String serviceName = MapUtils.getString(header, "serviceName");

        if (StringUtils.isAnyEmpty(email, url, serviceName)) {
            return null;
        }

        client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("USER_EMAIL", email);

        for (String key : body.keySet()) {
            String value = body.get(key);
            builder.addFormDataPart(key, value);
        }

        try {
            for (String key : files.keySet()) {
                String value = files.get(key);

                if (StringUtils.isNotBlank(value)) {
                    String[] split = StringUtils.split(value, ".");
                    String extension;

                    if (split.length > 0) {
                        extension = split[split.length - 1];
                    } else {
                        extension = "jpg";
                    }

                    builder.addFormDataPart(key, key + "." + extension, RequestBody.create(MEDIA_TYPE_IMG, new File(value)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultipartBody multipartBody = builder.build();

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
            resultList.add(result);
            Log.d("response", result);
        } catch (IOException e) {
            e.printStackTrace();
            resultList.add(e.getMessage());
        }
//        }

        return resultList;
    }

    public void setContext(Global global) {
        this.global = global;
    }
}
