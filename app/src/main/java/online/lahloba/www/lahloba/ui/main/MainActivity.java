package online.lahloba.www.lahloba.ui.main;

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

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.SliderItem;
import online.lahloba.www.lahloba.ui.adapters.SliderAdapter;
import online.lahloba.www.lahloba.ui.fragments.AccountFragment;
import online.lahloba.www.lahloba.ui.fragments.FavoriteFragment;
import online.lahloba.www.lahloba.ui.fragments.ShoppingFragment;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout indicator;
    BottomNavigationView bottomNavigationView;
    List<SliderItem> sliderItems;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        indicator=(TabLayout)findViewById(R.id.indicator);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sliderItems = new ArrayList<>();
        Uri uri = Uri.parse("http://www.lahloba.online/image/cache/catalog/Lahloba/Cover2-1140x380.png").buildUpon().build();
        sliderItems.add(new SliderItem(uri));

        uri = Uri.parse("http://www.lahloba.online/image/cache/catalog/Lahloba/Cover1-1140x380.png").buildUpon().build();
        sliderItems.add(new SliderItem(uri));


        SliderAdapter sliderAdapter = new SliderAdapter(this);
        sliderAdapter.setSliderItems(sliderItems);
        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager, true);

        if(Locale.getDefault().getLanguage().equals("ar")){
            indicator.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        Fragment fragment = new ShoppingFragment();
        loadFragment(fragment);

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
                    fragment = new AccountFragment();
                    loadFragment(fragment);
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


    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < sliderItems.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });

        }
    }
}
