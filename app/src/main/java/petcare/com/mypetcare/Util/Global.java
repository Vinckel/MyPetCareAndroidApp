package petcare.com.mypetcare.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KS on 2017-03-25.
 */

public class Global extends Application {
    private static volatile Global instance = null;
    private Map<String, Object> data;
    private static final long DAY_TO_MILLISECONDS = 86400000;
    private static final long HOUR_TO_MILLISECONDS = 3600000;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        data = new HashMap<>();
        data.put("token_api_calling", false);
        pref = getSharedPreferences("local_auth", MODE_PRIVATE);
        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public Map<String, Object> getMap() {
        return data;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getToken() {
        return data.containsKey("token") ? MapUtils.getString(data, "token") : null;
    }

    public String getEmail() {
        return data.containsKey("email") ? MapUtils.getString(data, "email") : null;
    }

    public boolean isValidToken() {
        long tokenDate = pref.getLong("auth_date", 0L);
        String token = pref.getString("token", null);
        long now = Calendar.getInstance().getTimeInMillis();

        return !(now - tokenDate > DAY_TO_MILLISECONDS - HOUR_TO_MILLISECONDS || StringUtils.isBlank(token));
    }

    public boolean isTokenApiCalling() {
        Boolean calling = MapUtils.getBoolean(data, "token_api_calling");

        return !(calling == null || !calling);
    }

    public static Global getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return Global.getGlobalApplicationContext();
                }
            };
        }
    }
}
