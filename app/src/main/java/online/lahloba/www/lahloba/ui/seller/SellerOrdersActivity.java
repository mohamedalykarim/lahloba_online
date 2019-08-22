package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.databinding.SellerOrderActivityBinding;
import online.lahloba.www.lahloba.utils.Injector;

public class SellerOrdersActivity extends AppCompatActivity {

    private SellerOrdersViewModel mViewModel;
    private List<MarketPlace> marketPlaces;
    boolean firstMarketplace = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_order_activity);
        SellerOrderActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.seller_order_activity);
        binding.setLifecycleOwner(this);

        ViewModelProviderFactory factory = Injector.getVMFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(SellerOrdersViewModel.class);


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);





        TabLayout tabLayout = binding.tabs;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (marketPlaces== null)return;
                setupMarketPlace(marketPlaces.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewModel.startGetMarketPlacesBySeller();



        mViewModel.getMarketPlaces().observe(this, marketPlaces -> {
            if (marketPlaces == null)return;

            if (firstMarketplace){
                setupMarketPlace(marketPlaces.get(0));
                firstMarketplace = false;
            }
            this.marketPlaces = marketPlaces;

            tabLayout.removeAllTabs();
            for (MarketPlace marketPlace : marketPlaces){
                tabLayout.addTab(tabLayout.newTab().setText(marketPlace.getName()));
            }
        });









    }

    private void setupMarketPlace(MarketPlace marketPlace) {
        SellerOrdersFragment fragment = new SellerOrdersFragment();
        fragment.setMarketPlace(marketPlace);

        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow();


    }
}
