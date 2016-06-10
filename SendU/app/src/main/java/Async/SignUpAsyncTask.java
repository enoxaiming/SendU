package Async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Data.UserInfo;
import HTTP.HttpUtil;
import handdev.sendu.Activity.MainActivity;

/**
 * Created by JunHyeok on 2016. 6. 10..
 */
public class SignUpAsyncTask extends AsyncTask<UserInfo, Void, Void> {

    private String URL = "http://sendyou.biz/userAuth/signup/insertData";
    private String res ="";
    @Override
    protected Void doInBackground(UserInfo... params) {

        UserInfo userInfo = (UserInfo)params[0];

        //URL = URL + "?username=" + userInfo.getUserName() + "&password=" + userInfo.getPassword();



        try {
            Map<String,String> param = new HashMap<>();
            param.put("username",userInfo.getUserName());
            param.put("password",userInfo.getPassword());
            param.put("email",userInfo.getEmail());
            param.put("phone",userInfo.getPhone());
            param.put("address",userInfo.getAddress());
            res = HttpUtil.postForm(URL,param);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(MainActivity.MainActivityContext, res, Toast.LENGTH_LONG).show();
    }
}
