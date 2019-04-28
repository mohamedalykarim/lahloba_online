package online.lahloba.www.lahloba.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.SliderItem;
import online.lahloba.www.lahloba.ui.adapters.SliderAdapter;
import online.lahloba.www.lahloba.ui.fragments.AccountFragment;
import online.lahloba.www.lahloba.ui.fragments.FavoriteFragment;
import online.lahloba.www.lahloba.ui.fragments.LoginFragment;
import online.lahloba.www.lahloba.ui.fragments.ShoppingFragment;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;
    LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewModelProviderFactory factory = Injector.getVMFactory(this,null);
        loginViewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);

        Fragment fragment = new ShoppingFragment();
        loadFragment(fragment);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.ic_action_shopping:
                    fragment = new ShoppingFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.ic_action_favorite:
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.ic_action_my_account:
                    loginAccountFragmetReplace();
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    public void loginAccountFragmetReplace(){
        loginViewModel.getIsLogged().observe(MainActivity.this ,isLogged->{
            if (isLogged){
                Fragment accountFragment = new AccountFragment();
                loadFragment(accountFragment);
            }else {
                Fragment loginFragment = new LoginFragment();
                loadFragment(loginFragment);
            }
        });
    }



}
