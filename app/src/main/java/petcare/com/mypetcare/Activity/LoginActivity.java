package petcare.com.mypetcare.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.apache.commons.collections4.MapUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.Global;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.TokenApi;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_CODE_LOCATION = 1;
    private CallbackManager callbackManager;
    private SharedPreferences pref;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001; // google
    protected static Global global = null;
    private ISessionCallback iSessionCallback;
    private OAuthLogin mOAuthLoginModule;

    private void saveEmail(String email) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("email", email);
        edit.apply();
    }

    private boolean checkEmail() {
        return pref.getString("email", null) != null;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String email = acct.getEmail();
            setEmailAndGoMain(email);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    private void setEmailAndGoMain(String email) {
        global.set("email", email);
        saveEmail(email);
        getToken(email);
        goToMain();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG" , "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            kakaoRequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("TAG" , exception.getMessage());
            }
        }
    }

    protected void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("kakaoLogin", userProfile.getEmail());
                setEmailAndGoMain(userProfile.getEmail());
                // 사용자정보 추출(완료)
//                profileUrl = userProfile.getProfileImagePath();
//                userId = String.valueOf(userProfile.getId());
//                userName = userProfile.getNickname();
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        global = (Global) getApplicationContext();
        pref = getSharedPreferences("local_auth", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        if (checkEmail()) {
            goToMain();
        }
        initializeNaverAPI();

        iSessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(iSessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();
//        Session.getCurrentSession().open(AuthType.KAKAO_TALK, LoginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

//        Button btGoogle = (Button) findViewById(R.id.bt_login_google);
//        btGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });

        SignInButton signInButton = (SignInButton) findViewById(R.id.bt_login_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("google", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLogin = (LoginButton) findViewById(R.id.bt_login_facebook);
        fbLogin.setReadPermissions("email");
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();
                Log.d("result", token.toString());
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("result", object.toString());
                        try {
                            Map<String, Object> map = GsonUtil.toMap(object);
                            String email = MapUtils.getString(map, "email");
                            setEmailAndGoMain(email);
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields", "email");
                graphRequest.setParameters(params);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("fbLoginCancel", "canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("fbLoginError", error.toString());
            }
        });
    }

    private void getToken(String email) {
        TokenApi tokenApi = new TokenApi();
        tokenApi.setContext(global);
        try {
            tokenApi.execute(email).get(1500L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Toast.makeText(LoginActivity.this, "인증 오류 발생", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(LoginActivity.this, "인증 오류", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (TimeoutException e) {
            Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(iSessionCallback);
    }

    private void initializeNaverAPI() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                this,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
        );

        // 네이버 로그인 버튼 리스너 등록
        OAuthLoginButton naverLoginButton = (OAuthLoginButton) findViewById(R.id.bt_login_naver);
        naverLoginButton.setOAuthLoginHandler(new OAuthLoginHandler() {
            @Override
            public void run(boolean b) {
                if (b) {
                    final String token = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String response = mOAuthLoginModule.requestApi(LoginActivity.this, token, "https://openapi.naver.com/v1/nid/me");
                            try {
                                JSONObject json = new JSONObject(response);
                                // response 객체에서 원하는 값 얻어오기
                                String email = json.getJSONObject("response").getString("email");
                                setEmailAndGoMain(email);
                                // 액티비티 이동 등 원하는 함수 호출
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                }
            }
        });
    }

    @TargetApi(23)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_LOCATION);

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            // writeFile();
        }
    }
}
