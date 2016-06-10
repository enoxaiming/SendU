package Async;

/**
 * Created by JunHyeok on 2016. 6. 10..
 */
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import org.json.*;


import Data.UserInfo;
import HTTP.HttpUtil;
import handdev.sendu.Activity.MainActivity;

public class LoginAsyncTask extends AsyncTask<UserInfo, Void, Void> {

    private String URL ="sendyou.biz/userAuth/signup/insertData";
    private String res = "";

    private String LOGTAG ="LoginAsyncTask";

    private boolean isAutoLogin = false;
    private final int MODE_PRIVATE = 0x0000;

    private UserInfo userInfo = null;
    //constructor
    // if isAutoLogin == true -> autoLogin when App starts
    public LoginAsyncTask(boolean isAutoLogin){
        this.isAutoLogin = isAutoLogin;
    }
    @Override
    protected Void doInBackground(UserInfo... params) {
        userInfo =(UserInfo)params[0];

        //URL = URL + "?username=" + userInfo.getUserName() + "&password=" + userInfo.getPassword();

        try {
            res = HttpUtil.get(URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Boolean isLoginSuccess = false;

        try {
            JSONObject jsonObject = new JSONObject(res);
            isLoginSuccess = jsonObject.getBoolean("success");
            Log.i(LOGTAG, isLoginSuccess.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //TODO save key String to String.xml
        if(isLoginSuccess){
            Log.i(LOGTAG, "isLoginSuccess : true");
            Log.i(LOGTAG, "isAutoLogin : "+ isAutoLogin);

            if(isAutoLogin) {
                //Set SharedPreferences
                SharedPreferences pref = MainActivity.mainActivity.getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("login", true);
                editor.apply();

                SharedPreferences userPref = MainActivity.mainActivity.getSharedPreferences("user", MODE_PRIVATE);
                editor = userPref.edit();
                editor.putString("username", userInfo.getUserName());
                editor.putString("password", userInfo.getPassword());
                editor.apply();

            }

            MainActivity.callCommingSoon();
            Log.i(LOGTAG, "isLoginSuccess : false");
        }

    }
}