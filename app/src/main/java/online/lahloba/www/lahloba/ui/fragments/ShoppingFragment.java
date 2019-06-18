package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.SliderItem;
import online.lahloba.www.lahloba.ui.adapters.ShoppingAdapter;
import online.lahloba.www.lahloba.ui.adapters.SliderAdapter;
import online.lahloba.www.lahloba.ui.main.MainActivity;
import online.lahloba.www.lahloba.ui.main.MainViewModel;
import online.lahloba.www.lahloba.utils.ExpandableHeightGridView;
import online.lahloba.www.lahloba.utils.Injector;

public class ShoppingFragment extends Fragment {

    MainViewModel mainViewModel;
    ViewPager viewPager;
    TabLayout indicator;
    List<SliderItem> sliderItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shopping_fragment,container,false);
        viewPager= view.findViewById(R.id.viewPager);
        indicator= view.findViewById(R.id.indicator);

        sliderItems = new ArrayList<>();
        SliderItem item = new SliderItem();
        item.setImageUri("http://www.lahloba.online/image/cache/catalog/Lahloba/Cover2-1140x380.png");
        item.setActivityName(".ui.products.ProductsActivity");
        item.setExtra("-LdKhIbxY8qwF5yypAOf");

        sliderItems.add(item);
        sliderItems.add(item);
        sliderItems.add(item);


        SliderAdapter sliderAdapter = new SliderAdapter(this.getContext());
        sliderAdapter.setSliderItems(sliderItems);
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
        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(), null);
        mainViewModel = ViewModelProviders.of(this.getActivity(),factory).get(MainViewModel.class);


        ExpandableHeightGridView gridView = view.findViewById(R.id.gridview);
        ShoppingAdapter shoppingAdapter = new ShoppingAdapter();


        gridView.setAdapter(shoppingAdapter);
        gridView.setExpanded(true);

        mainViewModel.getMainMenuItems().observe(this.getActivity(),items->{
            shoppingAdapter.setShoppingItemList(items);
            shoppingAdapter.notifyDataSetChanged();
        });





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
