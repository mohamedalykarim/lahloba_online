package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;

public class FavoriteItem extends BaseObservable {
    String productId;
    boolean enabled;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
