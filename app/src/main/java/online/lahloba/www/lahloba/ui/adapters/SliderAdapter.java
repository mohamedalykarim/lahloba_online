package online.lahloba.www.lahloba.ui.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.databinding.ItemSliderBinding;
import online.lahloba.www.lahloba.utils.Utils;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;
import static online.lahloba.www.lahloba.utils.Constants.SLIDER_ITEM_EXTRA;

public class SliderAdapter extends PagerAdapter {
    List<BannerItem> bannerItems;
    Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return null== bannerItems ? 0 : bannerItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemSliderBinding binding = ItemSliderBinding.inflate(inflater, container,false);
        ImageView imageView = binding.imageView;


        Picasso.get().load(bannerItems.get(position).getImageUrl()).into(imageView);

        Log.v("mmm", bannerItems.get(position).getImageUrl());

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerItems.get(position).getType() != null){



                    Intent intent = new Intent();
                    String packageName = "online.lahloba.www.lahloba";
                    String className = packageName+ Utils.getBannerActivityName(bannerItems.get(position).getType());
                    intent.setComponent(new ComponentName(binding.getRoot().getContext(),className));

                    if (bannerItems.get(position).getExtra() != null){
                        if (className.contains("ProductsActivity")){
                            intent.putExtra(EXTRA_SUBTITLE_ID, bannerItems.get(position).getExtra());
                        }else if (className.contains("NewsActivity")){
                            intent.putExtra(EXTRA_SUBTITLE_ID, bannerItems.get(position).getExtra());
                        }else {
                            intent.putExtra(SLIDER_ITEM_EXTRA, bannerItems.get(position).getExtra());
                        }

                    }

                    container.getContext().startActivity(intent);
                }
            }
        });


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(binding.getRoot(), 0);

        return binding.getRoot();

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    public void setBannerItems(List<BannerItem> bannerItems) {
        this.bannerItems = bannerItems;
    }
}
