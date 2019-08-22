package online.lahloba.www.lahloba.data.model.vm_helper;

import androidx.lifecycle.MutableLiveData;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.UserItem;


public class LoginVMHelper extends BaseObservable {
    MutableLiveData<Boolean> isLogged;
    UserItem currentUser;

    public LoginVMHelper() {
        isLogged = new MutableLiveData<>();
    }


    @Bindable
    public MutableLiveData<Boolean> getIsLogged() {
        return isLogged;
    }

    @Bindable
    public UserItem getCurrentUser() {
        return currentUser;
    }

    public void setLogged(boolean logged) {
        isLogged.setValue(logged);
        notifyPropertyChanged(BR.isLogged);
    }

    public void setCurrentUser(UserItem currentUser) {
        this.currentUser = currentUser;
        notifyPropertyChanged(BR.currentUser);
    }
}
