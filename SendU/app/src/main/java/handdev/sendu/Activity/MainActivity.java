package handdev.sendu.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import handdev.sendu.R;


public class MainActivity extends FragmentActivity implements MainFragment.OnFragmentInteractionListener{

    private CallbackManager callbackManager;
    private String LOGTAG = "MainActivity";
    public static Context MainActivityContext;
    public static MainActivity mainActivity;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startActivity(new Intent(this,SplashActivity.class));

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        MainActivityContext = getApplicationContext();
        mainActivity = this;

        FragmentManager fragmentManager = getFragmentManager();
        Button button = (Button)findViewById(R.id.button);

        //getFragmentManager().beginTransaction().replace(R.id.frag_main, new MainFragment()).commit();

        Log.i(LOGTAG, "login pref :" + pref.getBoolean("login", false));
        Log.i(LOGTAG, "tutorial pref : " + pref.getBoolean("tutorial", true));
        if(!pref.getBoolean("login",false) && pref.getBoolean("tutorial", true)) {

            Log.i(LOGTAG, "Show Tutorial");
            final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_main, onBoardingFragment);
            fragmentTransaction.commit();

            onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                @Override
                public void onRightOut() {
                    getFragmentManager().beginTransaction().replace(R.id.frag_main, onBoardingFragment).commit();

                }
            });
        }else if(!pref.getBoolean("login", false) && !pref.getBoolean("tutorial", true)){
            Log.i(LOGTAG, "Show Login");

            final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frag_main, onBoardingFragment);
            fragmentTransaction.commit();

            onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
                @Override
                public void onRightOut() {
                    getFragmentManager().beginTransaction().replace(R.id.frag_main, onBoardingFragment).commit();

                }
            });

            //getFragmentManager().beginTransaction().add(R.id.frag_main, new MainFragment()).commit();
        }
        else{
            callCommingSoon();
        }

        //Facebook Start
        callbackManager = CallbackManager.Factory.create();
        LoginButton facebookButton = (LoginButton)findViewById(R.id.login_button);
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("login", true);
                editor.apply();
                startActivity(new Intent(MainActivity.this,CommingSoon.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //FaceBook End



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



   /* @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/



    /*public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }*/

    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("tutorial", false);
        editor.apply();

        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Hotels", "All hotels and hostels are sorted by hospitality rating",
                Color.parseColor("#678FB4"), R.drawable.ic_get_app_white_24dp, R.drawable.ic_keyboard_black_24dp);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Banks", "We carefully verify all banks before add them into the app",
                Color.parseColor("#65B0B4"), R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Stores", "All local stores are categorized for your convenience",
                Color.parseColor("#9B90BC"), R.drawable.letter, R.drawable.ic_menu_send);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;

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

    public static void callCommingSoon(){
        Intent intent = new Intent(MainActivity.mainActivity, CommingSoon.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainActivityContext.startActivity(intent);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
