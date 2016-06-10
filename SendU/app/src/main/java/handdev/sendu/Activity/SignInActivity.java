package handdev.sendu.Activity;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import Data.UserInfo;
import handdev.sendu.R;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        final UserInfo userInfo = new UserInfo();
        passwordEditText = (EditText)findViewById(R.id.password);
        emailEditText = (EditText) findViewById(R.id.email);
    }
}
