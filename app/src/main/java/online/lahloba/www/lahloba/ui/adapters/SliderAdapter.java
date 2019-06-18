package online.lahloba.www.lahloba.ui.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.SliderItem;
import online.lahloba.www.lahloba.databinding.ItemSliderBinding;
import online.lahloba.www.lahloba.ui.products.ProductsActivity;
import online.lahloba.www.lahloba.utils.Constants;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;
import static online.lahloba.www.lahloba.utils.Constants.SLIDER_ITEM_EXTRA;

public class SliderAdapter extends PagerAdapter {
    List<SliderItem> sliderItems;
    Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return null==sliderItems ? 0 : sliderItems.size();
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

        Picasso.get().load(sliderItems.get(position).getImageUri()).into(imageView);


        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sliderItems.get(position).getActivityName() != null){
                    Intent intent = new Intent();
                    String packageName = "online.lahloba.www.lahloba";
                    String className = packageName+sliderItems.get(position).getActivityName();
                    intent.setComponent(new ComponentName(binding.getRoot().getContext(),className));

                    if (sliderItems.get(position).getExtra() != null){
                        if (className.contains("ProductsActivity")){
                            intent.putExtra(EXTRA_SUBTITLE_ID, sliderItems.get(position).getExtra());
                        }else {
                            intent.putExtra(SLIDER_ITEM_EXTRA, sliderItems.get(position).getExtra());
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

    public void setSliderItems(List<SliderItem> sliderItems) {
        this.sliderItems = sliderItems;
    }
}
