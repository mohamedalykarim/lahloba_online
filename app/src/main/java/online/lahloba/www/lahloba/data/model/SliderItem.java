package online.lahloba.www.lahloba.data.model;

import android.net.Uri;

public class SliderItem {
    String imageUri;
    String activityName;
    String extra;

    public String getImageUri() {
        return imageUri;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getExtra() {
        return extra;
    }


    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
