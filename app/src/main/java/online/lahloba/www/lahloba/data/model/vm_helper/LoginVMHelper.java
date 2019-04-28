package online.lahloba.www.lahloba.data.model.vm_helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.UserItem;


public class LoginVMHelper extends BaseObservable {
    boolean isLogged;
    UserItem currentUser;

    @Bindable
    public boolean isLogged() {
        return isLogged;
    }

    @Bindable
    public UserItem getCurrentUser() {
        return currentUser;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
        notifyPropertyChanged(BR.logged);
    }

    public void setCurrentUser(UserItem currentUser) {
        this.currentUser = currentUser;
        notifyPropertyChanged(BR.currentUser);
    }
}
