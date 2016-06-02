package handdev.sendu.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

import handdev.sendu.R;

public class LoginActivity extends AppCompatActivity {



    public static LoginActivity loginActivity;
    public static FragmentManager fragmentManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this, SplashActivity.class));
        final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());
        fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.loginMainFrameLayout, onBoardingFragment);
        fragmentTransaction.commit();
        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                //SelectServiceFragment bf = SelectServiceFragment.newInstance(LoginActivity.this);
                //getSupportFragmentManager().beginTransaction().add(R.id.loginMainFrameLayout, bf).commit();
            }
        });
    }

    //onboarding
    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        // prepare data
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Hotels", "All hotels and hostels are sorted by hospitality rating",
                Color.parseColor("#678FB4"), R.drawable.network, R.drawable.letter);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Banks", "We carefully verify all banks before add them into the app",
                Color.parseColor("#65B0B4"), R.drawable.social, R.drawable.social);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Stores", "All local stores are categorized for your convenience",
                Color.parseColor("#9B90BC"), R.drawable.letter, R.drawable.network);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;
    }



}
