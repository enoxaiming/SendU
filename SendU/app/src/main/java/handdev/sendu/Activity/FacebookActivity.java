package handdev.sendu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import handdev.sendu.R;

public class FacebookActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button login = (Button)findViewById(R.id.login);
        if(login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        }

        Button logout = (Button)findViewById(R.id.logout);
        if(logout != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

        }





        mCallbackManager = CallbackManager.Factory.create();
        LoginButton facebookButton = (LoginButton) findViewById(R.id.login_button);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        facebookButton.setReadPermissions("public_profile");
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(FacebookActivity.this,CommingSoon.class));
            }

            @Override
            public void onCancel() {
                ((TextView) findViewById(R.id.status)).setText("Status: canceled");
            }

            @Override
            public void onError(FacebookException error) {
                startActivity(new Intent(FacebookActivity.this,CommingSoon.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login() {
        ((TextView) findViewById(R.id.status)).setText("Status: logging in");
    }

    public void logout() {
        ((TextView) findViewById(R.id.status)).setText("Status: logged out");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
