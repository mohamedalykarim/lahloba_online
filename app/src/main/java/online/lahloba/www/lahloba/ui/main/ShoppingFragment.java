package online.lahloba.www.lahloba.ui.main;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.ui.adapters.ShoppingAdapter;
import online.lahloba.www.lahloba.ui.adapters.SliderAdapter;
import online.lahloba.www.lahloba.utils.ExpandableHeightGridView;
import online.lahloba.www.lahloba.utils.Injector;

public class ShoppingFragment extends Fragment {
    MainViewModel mainViewModel;
    ViewPager viewPager;
    TabLayout indicator;
    List<BannerItem> bannerItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping,container,false);
        viewPager= view.findViewById(R.id.viewPager);
        indicator= view.findViewById(R.id.indicator);


        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext());
        mainViewModel = ViewModelProviders.of(this.getActivity(),factory).get(MainViewModel.class);


        ExpandableHeightGridView gridView = view.findViewById(R.id.gridview);
        ShoppingAdapter shoppingAdapter = new ShoppingAdapter();


        gridView.setAdapter(shoppingAdapter);
        gridView.setExpanded(true);

        mainViewModel.getMainMenuItems().observe(this.getActivity(),items->{
            shoppingAdapter.setShoppingItemList(items);
            shoppingAdapter.notifyDataSetChanged();
        });




        bannerItems = new ArrayList<>();


        SliderAdapter sliderAdapter = new SliderAdapter(this.getContext());


        mainViewModel.startGetBanner();
        mainViewModel.getBannerItems().observe(this, bannerItemsList -> {
            if (bannerItemsList == null) return;
            bannerItems.clear();
            bannerItems.addAll(bannerItemsList);

            sliderAdapter.setBannerItems(bannerItems);
            sliderAdapter.notifyDataSetChanged();


        });

        viewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewPager, true);

        if(Locale.getDefault().getLanguage().equals("ar")){
            indicator.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {





        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.startGetMainMenuItems();
    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < bannerItems.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });


        }
    }
}
