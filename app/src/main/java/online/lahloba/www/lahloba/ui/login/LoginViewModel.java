package online.lahloba.www.lahloba.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.LoginVMHelper;
import online.lahloba.www.lahloba.utils.Injector;

public class LoginViewModel extends ViewModel {
    AppRepository appRepository;
    public LoginVMHelper loginVMHelper;

    public LoginViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        loginVMHelper = new LoginVMHelper();

        getIsLogged().observeForever(isLogged->{
            if(isLogged == null)return;

            if (isLogged){
                startGetUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

//        getCurrentUserDetails().observeForever(currentUser->{
//            if (null != currentUser){
//                loginVMHelper.setCurrentUser(currentUser);
//            }
//        });
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
        loginVMHelper.setLogged(false);
    }


    public MutableLiveData<Boolean> getIsLogged(){
        return appRepository.getIsLogged();
    }

    public void deleteLocalCartItems() {
        Injector.getExecuter().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appRepository.deleteAllFromInternalCart();
            }
        });

    }
}
