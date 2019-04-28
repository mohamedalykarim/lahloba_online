package online.lahloba.www.lahloba.ui.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.LoginVMHelper;

public class LoginViewModel extends ViewModel {
    AppRepository appRepository;
    LoginVMHelper loginVMHelper;

    public LoginViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        loginVMHelper = new LoginVMHelper();

        getIsLogged().observeForever(isLogged->{
            loginVMHelper.setLogged(isLogged);

            if (isLogged){
                startGetUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

        getCurrentUserDetails().observeForever(currentUser->{
            if (null != currentUser){
                loginVMHelper.setCurrentUser(currentUser);
            }
        });
    }


    public void startGetUserDetails(String uid) {
        appRepository.startGetUserDetails(uid);
    }

    public MutableLiveData<UserItem> getCurrentUserDetails(){
        return appRepository.getCurrentUserDetails();
    }
    public void startLogin(String email, String password) {
        appRepository.startLogin(email, password);
    }

    public void startLogOut(){
        appRepository.startLogout();
    }

    public MutableLiveData<Boolean> getIsLogged(){
        return appRepository.getIsLogged();
    }
}
