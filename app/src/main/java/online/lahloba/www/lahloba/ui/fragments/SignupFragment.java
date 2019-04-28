package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.signup.SignupViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class SignupFragment extends Fragment {

    private SignupViewModel mViewModel;
    private LoginViewModel loginViewModel;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(),null);
        mViewModel = ViewModelProviders.of(this, factory).get(SignupViewModel.class);

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext(),null);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        loginViewModel.getIsLogged().observe(this,isLogged->{
            if (isLogged){
                this.getActivity().finish();
            }
        });
    }
}
