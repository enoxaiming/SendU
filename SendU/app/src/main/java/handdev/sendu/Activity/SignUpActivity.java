package handdev.sendu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import async.SignUpAsyncTask;
import data.UserInfo;
import handdev.sendu.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final UserInfo userInfo = new UserInfo();

        userNameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
        emailEditText = (EditText) findViewById(R.id.email);
        phoneEditText = (EditText) findViewById(R.id.phone);
        addressText = (EditText) findViewById(R.id.address);


        Button button = (Button)findViewById(R.id.signin);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfo.setUserName(String.valueOf(userNameEditText.getText()));
                    userInfo.setPassword(String.valueOf(passwordEditText.getText()));
                    userInfo.setEmail(String.valueOf(emailEditText.getText()));
                    userInfo.setAddress(String.valueOf(addressText.getText()));
                    userInfo.setPhone(String.valueOf(phoneEditText.getText()));
                    SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
                    signUpAsyncTask.execute(userInfo);
                }
            });

        }

    }


}
