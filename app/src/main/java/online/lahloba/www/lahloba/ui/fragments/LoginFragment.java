package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.util.StringUtil;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.FragmentLoginBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.signup.SignupActivity;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Utils.checkValidEmail;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    TextView emailET, passwordET;
    FragmentLoginBinding binding;
    int error = 0;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false);
        emailET = binding.emailET;
        passwordET = binding.passwordET;

        createAccount();
        login();

        View view = binding.getRoot();
        return view;
    }

    private void login() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkloginInput(emailET.getText().toString(), passwordET.getText().toString());
                if (error==0){
                    mViewModel.startLogin(emailET.getText().toString(), passwordET.getText().toString());
                }
            }
        });
    }

    private void checkloginInput(String email, String password) {
        if (email.equals("") || null==email || !checkValidEmail(email) ){
            emailET.setError("Please Enter Valid Email Address");
            error++;
        }

        if (password.equals("") || null == password){
            passwordET.setError("Please Enter Valid Password");
            error++;
        }


        if (password.length() > 0 && password.length() < 6 ){
            passwordET.setError("Password length must be between 6 to 10 character");
            error++;
        }

        if (password.length() > 10 ){
            passwordET.setError("Password length must be between 6 to 10 character");
            error++;
        }

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext(),null);
        mViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    public void createAccount(){
        binding.newAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }





}
