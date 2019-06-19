package online.lahloba.www.lahloba.data.model;

import android.net.Uri;

public class BannerItem {
    String imageUrl;
    String type;
    String extra;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getExtra() {
        return extra;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
