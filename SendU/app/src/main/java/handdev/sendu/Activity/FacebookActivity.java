package handdev.sendu.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import handdev.sendu.R;

public class FacebookActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    /*private static FacebookActivity mInstance = null;
    private static Activity mActivity;
    public static FacebookActivity getInstance(Activity activity){
        if (mInstance==null)
            synchronized (FacebookActivity.class) {
                if (mInstance==null)
                    mInstance=new FacebookActivity(activity);
            }
        if (activity!=null&&!mInstance.mActivity.equals(activity))
            mInstance.mActivity=activity;
        return mInstance;
    }*/

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
                ((TextView) findViewById(R.id.status)).setText("Status: error");
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
}
