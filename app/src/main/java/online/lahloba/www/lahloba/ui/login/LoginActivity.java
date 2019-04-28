package online.lahloba.www.lahloba.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commitNow();
        }
    }
}
