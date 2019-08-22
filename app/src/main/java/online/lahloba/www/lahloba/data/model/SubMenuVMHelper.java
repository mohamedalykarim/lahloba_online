package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class SubMenuVMHelper extends BaseObservable {
    private boolean isLoading;

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }
}
