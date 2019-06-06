package online.lahloba.www.lahloba.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;


import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.ActivityMainBinding;
import online.lahloba.www.lahloba.ui.fragments.AccountFragment;
import online.lahloba.www.lahloba.ui.fragments.FavoriteFragment;
import online.lahloba.www.lahloba.ui.login.LoginFragment;
import online.lahloba.www.lahloba.ui.fragments.ShoppingFragment;
import online.lahloba.www.lahloba.ui.governerate.GovernerateActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.order.OrdersActivity;
import online.lahloba.www.lahloba.utils.Injector;

public class MainActivity extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        binding.toolbar.setTitle(getResources().getString(R.string.app_name));
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_my_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewModelProviderFactory factory = Injector.getVMFactory(this, null);
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);

        Fragment fragment = new ShoppingFragment();
        loadFragment(fragment);


//
//        String push = FirebaseDatabase.getInstance().getReference().child("MarketPlace").push().getKey();
//        MarketPlaceItem marketPlaceItem = new MarketPlaceItem();
//        marketPlaceItem.setId(push);
//        marketPlaceItem.setName("اولاد رجب حلوان");
//        marketPlaceItem.setSellerId("sellerId002");
//        marketPlaceItem.setSellerName("اولاد رجب");
//        FirebaseDatabase.getInstance().getReference().child("MarketPlace/"+push).setValue(marketPlaceItem);
//
//        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("MarketPlaceLocation");
//        GeoFire geoFire = new GeoFire(firebaseDatabase);
//        geoFire.setLocation(push, new GeoLocation(29.821747, 31.324114), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                Toast.makeText(MainActivity.this, "one" , Toast.LENGTH_SHORT).show();
//            }
//        });

        startActivity(new Intent(this, OrdersActivity.class));

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

    public void loginAccountFragmetReplace() {
        loginViewModel.getIsLogged().observe(MainActivity.this, isLogged -> {
            if (isLogged) {
                Fragment accountFragment = new AccountFragment();
                loadFragment(accountFragment);
            } else {
                Fragment loginFragment = new LoginFragment();
                loadFragment(loginFragment);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, GovernerateActivity.class);
                startActivity(intent);


                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
