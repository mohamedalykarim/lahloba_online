package online.lahloba.www.lahloba.ui.signup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.AppRepository;

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
