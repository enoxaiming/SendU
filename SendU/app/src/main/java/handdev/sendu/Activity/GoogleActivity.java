package handdev.sendu.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import handdev.sendu.R;

public class GoogleActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int REQUEST_CODE_RESOLVE = 1;
    private static final int REQUEST_CODE_EMAIL = 2;
    private static final int REQUEST_CODE_TOKEN = 3;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private boolean mBusy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button login = (Button) findViewById(R.id.login);
        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        }

        Button logout = (Button) findViewById(R.id.logout);
        if (logout != null) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();





    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            TextView textView = ((TextView)findViewById(R.id.status));
            if(textView != null && acct != null)
                textView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

        } else {
            // Signed out, show unauthenticated UI.

        }
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
    protected void onStop() {
        super.onStop();
        Log.d("GoogleActivity", "onStop");

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public void login() {
        Log.d("GoogleActivity", "login");

        if (mBusy) {
            return;
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(new Scope(Scopes.PROFILE))
                    .addScope(new Scope(Scopes.EMAIL))
                    .build();
        }
        if (!mGoogleApiClient.isConnected()) {
            mBusy = true;
            ((TextView) findViewById(R.id.status)).setText("Status: connecting");
            mGoogleApiClient.connect();
        }
    }

    public void logout() {
        Log.d("GoogleActivity", "logout");

        if (mBusy) {
            return;
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mBusy = true;
            ((TextView) findViewById(R.id.status)).setText("Status: logging out");
            ((TextView) findViewById(R.id.email)).setText("Email: ");
            ((TextView) findViewById(R.id.token)).setText("Token: ");
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.d("GoogleActivity", "revokeAccessAndDisconnect onResult: status = " + status);
                            mGoogleApiClient.disconnect();
                            mBusy = false;
                            ((TextView) findViewById(R.id.status)).setText("Status: revoked");
                        }
                    });
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GoogleActivity", "onConnected");
        ((TextView) findViewById(R.id.status)).setText("Status: connected");

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (person != null) {
            String name = person.getDisplayName();
            ((TextView) findViewById(R.id.status)).setText("Status: connected as " + name);
        }

        requestEmail();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleActivity", "onConnectionSuspended: i = " + i);
        ((TextView) findViewById(R.id.status)).setText("Status: connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GoogleActivity", "onConnectionFailed: connectionResult = " + connectionResult);

        if (connectionResult.hasResolution()) {
            try {
                ((TextView) findViewById(R.id.status)).setText("Status: resolving");
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mBusy = false;
        ((TextView) findViewById(R.id.status)).setText("Status: connection failed");
    }

    public void requestEmail() {
        Log.d("GoogleActivity", "requestEmail");

        String permission = Manifest.permission.GET_ACCOUNTS;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            ((TextView) findViewById(R.id.email)).setText("Email: (getting)");
            getEmail();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                mBusy = false;
                ((TextView) findViewById(R.id.email)).setText("Email: (denied)");
            } else {
                ((TextView) findViewById(R.id.email)).setText("Email: (requesting permission)");
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE_EMAIL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("GoogleActivity", "onRequestPermissionsResult: requestCode = " + requestCode);

        if (requestCode == REQUEST_CODE_EMAIL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getEmail();
            }
        }
    }

    public void getEmail() {
        Log.d("GoogleActivity", "getEmail");

        try {
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            if (email != null) {
                ((TextView) findViewById(R.id.email)).setText("Email: " + email);
                getToken(email);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBusy = false;
        ((TextView) findViewById(R.id.email)).setText("Email: (failed)");
    }

    public void getToken(final String email) {
        Log.d("GoogleActivity", "getToken: email = " + email);
        ((TextView) findViewById(R.id.token)).setText("Token: (getting)");

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String GOOGLE_SCOPE = "oauth2:" + Scopes.PLUS_LOGIN + " https://www.googleapis.com/auth/userinfo.email";
                Bundle GOOGLE_ACTIVITIES = new Bundle();
                GOOGLE_ACTIVITIES.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES, "http://schemas.google.com/AddActivity");
                try {
                    return GoogleAuthUtil.getToken(GoogleActivity.this, email, GOOGLE_SCOPE, GOOGLE_ACTIVITIES);
                } catch (UserRecoverableAuthException e) {
                    startActivityForResult(e.getIntent(), REQUEST_CODE_TOKEN);
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String token) {
                Log.d("GoogleActivity", "onPostExecute: token = " + token);

                if (token == null) {
                    mBusy = false;
                    ((TextView) findViewById(R.id.token)).setText("Token: (failed)");
                } else if (token.length() == 0) {
                    ((TextView) findViewById(R.id.token)).setText("Token: (recovering)");
                } else {
                    useToken(token);
                }
            }
        };
        task.execute();
    }

    public void useToken(String token) {
        Log.d("GoogleActivity", "useToken: token = " + token);
        mBusy = false;
        ((TextView) findViewById(R.id.token)).setText("Token: " + token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
