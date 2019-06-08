package online.lahloba.www.lahloba.utils;

import java.util.Locale;

public class LocalUtils {
    public static String getLangauge(){
        String language = Locale.getDefault().getLanguage();
        if (language.equals("en") || language.equals("ar")){
            return language;
        }

        return "en";

    }
}
