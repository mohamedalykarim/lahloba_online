package online.lahloba.www.lahloba.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.DateUtils;
import android.util.TypedValue;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import online.lahloba.www.lahloba.ui.products.ProductsActivity;

public class Utils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static boolean checkValidEmail(String email){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches()){
            return true;
        }else{
            return false;
        }

    }

    public static int getCostByDistance(double distance){
        if (distance <= 5){
            return 10;
        }else if (distance >5 && distance <= 10){
            return 15;
        }else if (distance >10 && distance <= 15){
            return 20;
        }else if (distance >15 && distance <= 20){
            return 25;
        }else if (distance > 20){
            return 30;
        }

        return 0;

    }


    public static String getBannerActivityName(String type){
        if (type.equals(Constants.BANNER_TYPE_PRODUCTS_CATEGORY)){
            return ".ui.products.ProductsActivity";
        }else if (type.equals(Constants.BANNER_TYPE_NEW_NEWS)){
            return ".ui.news.NewsActivity";
        }else {
            return "";
        }
    }


    public static String getRelationTime(Date past) {
        if (past == null) return "";

        long now = System.currentTimeMillis();
        long difference = now - past.getTime();

        if (difference > 0 && difference < DateUtils.MINUTE_IN_MILLIS){
            return (String) DateUtils.getRelativeTimeSpanString(past.getTime(), now, DateUtils.SECOND_IN_MILLIS);
        }else if (difference >= DateUtils.MINUTE_IN_MILLIS && difference < DateUtils.HOUR_IN_MILLIS){
            return (String) DateUtils.getRelativeTimeSpanString(past.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
        }else if (difference >= DateUtils.HOUR_IN_MILLIS && difference < DateUtils.DAY_IN_MILLIS){
            return (String) DateUtils.getRelativeTimeSpanString(past.getTime(), now, DateUtils.HOUR_IN_MILLIS);
        }else if ( difference >= DateUtils.DAY_IN_MILLIS){
            return (String) DateUtils.getRelativeTimeSpanString(past.getTime(), now, DateUtils.DAY_IN_MILLIS);
        }else {
            return (String) android.text.format.DateFormat.format("hh:mm:ss a", past);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
