package online.lahloba.www.lahloba.data.model.vm_helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import online.lahloba.www.lahloba.BR;


public class LoginVMHelper extends BaseObservable {
    boolean isLogged;

    @Bindable
    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
        notifyPropertyChanged(BR.logged);
    }
}
