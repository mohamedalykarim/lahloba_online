package online.lahloba.www.lahloba.data.model;

import android.net.Uri;

public class SliderItem {
    Uri uri;


    public SliderItem(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
