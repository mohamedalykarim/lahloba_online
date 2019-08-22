package online.lahloba.www.lahloba.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SignupFragment.newInstance())
                    .commitNow();
        }


    }
}
