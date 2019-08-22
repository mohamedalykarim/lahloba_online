package online.lahloba.www.lahloba.utils;


import androidx.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;

public class CustomSetter {
    static Typeface type;

    public static final String FONT_HACEN_LINER_XXL = "fonts/Hacen Liner XXL.ttf";
    public static final String FONT_EXO2_BOLD = "fonts/Exo2-Bold.otf";
    public static final String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String FONT_ROBOTO_BLACK = "fonts/Roboto-Black.ttf";



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

    @BindingAdapter("android:setImage")
    public static void setImage(ImageView imageView, int image){
            Picasso.get().load(image).into(imageView);
    }


    @BindingAdapter("android:shippingIcon")
    public static void setShippingIcon(ImageView imageView, String shippingType){
        if (shippingType.equals(CartVMHelper.FREE_SHIPPING)){
            Picasso.get().load(R.drawable.free_shipping).placeholder(R.drawable.progress_animation).into(imageView);
        }else if (shippingType.equals(CartVMHelper.HYPERLOCAL_SHIPPING)){
            Picasso.get().load(R.drawable.hyperlocal_icon).placeholder(R.drawable.progress_animation).into(imageView);
        }


    }

    @BindingAdapter("android:orderStatusCategory")
    public static void setOrderStatusCategory(TextView textView, int order){
        if (order == 1){
            textView.setText(textView.getResources().getString(R.string.new_order));
        }else if (order == 2){
            textView.setText(textView.getResources().getString(R.string.ongoing_order));
        }else if (order == 3){
            textView.setText(textView.getResources().getString(R.string.ongoing_order));
        }else if (order == 4){
            textView.setText(textView.getResources().getString(R.string.ongoing_order));
        }else if (order == 5){
            textView.setText(textView.getResources().getString(R.string.old_order));
        }
    }

    @BindingAdapter("android:orderStatus")
    public static void setOrderStatus(TextView textView, int order){
        if (order == 1){
            textView.setText(textView.getResources().getString(R.string.pending_order));
        }else if (order == 2){
            textView.setText(textView.getResources().getString(R.string.processing_order));
        }else if (order == 3){
            textView.setText(textView.getResources().getString(R.string.processed_order));
        }else if (order == 4){
            textView.setText(textView.getResources().getString(R.string.shipped_order));
        }else if (order == 5){
            textView.setText(textView.getResources().getString(R.string.completed_order));
        }else if (order == 65){
            textView.setText(textView.getResources().getString(R.string.order_recieved));
        }
    }

    @BindingAdapter("android:orderStatusImage")
    public static void setOrderStatusImage(ImageView imageView, int order){
        if (order == 1){
            Picasso.get().load(R.drawable.order_pending_icon).into(imageView);
        }else if (order == 2){
            Picasso.get().load(R.drawable.order_processing_icon).into(imageView);
        }else if (order == 3){
            Picasso.get().load(R.drawable.order_processed_icon).into(imageView);
        }else if (order == 4){
            Picasso.get().load(R.drawable.order_shipped_icon).into(imageView);
        }else if (order == 5){
            Picasso.get().load(R.drawable.order_completed_icon).into(imageView);
        }else if (order == 6){
            Picasso.get().load(R.drawable.order_recieved_icon).into(imageView);
        }
    }

    @BindingAdapter("android:orderDate")
    public static void setOrderDate(TextView textView, Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        textView.setText(simpleDateFormat.format(date));
    }

    @BindingAdapter("android:orderTime")
    public static void setOrderTime(TextView textView, Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmms");
        textView.setText(Utils.getRelationTime(date));
    }



    public static String getSelectedFont(){
        if (LocalUtils.getLangauge().equals("en")){
            return FONT_ROBOTO_BLACK;
        }else if (LocalUtils.getLangauge().equals("ar")){
            return FONT_HACEN_LINER_XXL;
        }
        return FONT_EXO2_BOLD;
    }





}
