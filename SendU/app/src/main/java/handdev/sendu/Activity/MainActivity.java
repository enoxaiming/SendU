package handdev.sendu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;

import handdev.sendu.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button google = (Button)findViewById(R.id.google);
        if(google != null) {
            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,GoogleActivity.class));
                }
            });
        }


        Button facebook = (Button)findViewById(R.id.facebook);
        if(facebook != null) {
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,FacebookActivity.class));
                }
            });
        }
    }
}
