package online.lahloba.www.lahloba.data.model;

import android.net.Uri;

public class MainMenuItem {
    int arrange;
    String image;
    String title;
    String id;

    public MainMenuItem() {
    }



    public int getArrange() {
        return arrange;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setArrange(int arrange) {
        this.arrange = arrange;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }
}
