package online.lahloba.www.lahloba.utils;


import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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




}
