package online.lahloba.www.lahloba.utils;


import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;

public class CustomSetter {
    static Typeface type;

    public static final String FONT_HACEN_LINER_XXL = "fonts/Hacen Liner XXL.ttf";

@BindingAdapter("android:fontType")
public static void setFont(View view, String font){
    if (type==null){
        type = Typeface.createFromAsset(view.getContext().getAssets(),font);
    }

    if (view instanceof TextView){
        ((TextView) view).setTypeface(type);
    }else if (view instanceof EditText){
        ((EditText) view).setTypeface(type);
    }else if (view instanceof Button){
        ((Button) view).setTypeface(type);
    }
}

    @BindingAdapter("android:shippingIcon")
    public static void setShippingIcon(ImageView imageView, String shippingType){
    if (shippingType.equals(CartVMHelper.FREE_SHIPPING)){
        Picasso.get().load(R.drawable.free_shipping).placeholder(R.drawable.progress_animation).into(imageView);
    }else if (shippingType.equals(CartVMHelper.HYPERLOCAL_SHIPPING)){
        Picasso.get().load(R.drawable.hyperlocal_icon).placeholder(R.drawable.progress_animation).into(imageView);
    }


    }





}
