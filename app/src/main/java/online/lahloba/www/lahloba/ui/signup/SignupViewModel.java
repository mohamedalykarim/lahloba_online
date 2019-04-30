package online.lahloba.www.lahloba.ui.signup;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.AppRepository;

public class SignupViewModel extends ViewModel {
    AppRepository appRepository;
    public SignupViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void createNewAccount(String firstName, String secondName,
                                        String phone, String email, String password) {
        appRepository.createNewAccount(firstName,secondName,phone, email,password);
    }

    public MutableLiveData<Boolean> getIsUserCreated() {
        return appRepository.getIsUserCreated();
    }

    public void addCartItemsToFireBase(String userId) {
        appRepository.addCartItemsToFireBase(userId);
    }
}
