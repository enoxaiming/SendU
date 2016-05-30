package handdev.sendu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //onboarding
    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Hotels", "All hotels and hostels are sorted by hospitality rating",
                Color.parseColor("#678FB4"), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Banks", "We carefully verify all banks before add them into the app",
                Color.parseColor("#65B0B4"), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Stores", "All local stores are categorized for your convenience",
                Color.parseColor("#9B90BC"), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

    }
}
