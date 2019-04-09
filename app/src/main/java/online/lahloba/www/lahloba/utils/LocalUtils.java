package online.lahloba.www.lahloba.utils;

import java.util.Locale;

public class LocalUtils {
    public static String getLangauge(){
        return Locale.getDefault().getLanguage();
    }
}
