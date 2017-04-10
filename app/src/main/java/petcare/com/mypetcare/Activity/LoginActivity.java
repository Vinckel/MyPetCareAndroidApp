package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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

import org.apache.commons.collections4.MapUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.Global;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.TokenApi;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private CallbackManager callbackManager;
    private SharedPreferences pref;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001; // google
    protected static Global global = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        global = (Global) getApplicationContext();
        pref = getSharedPreferences("local_auth", MODE_PRIVATE);
        if (checkEmail()) {
            goToMain();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
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

        Button fbLogin = (Button) findViewById(R.id.bt_login_facebook);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    }
}
