package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.FragmentSignupBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.signup.SignupViewModel;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Utils.checkValidEmail;

public class SignupFragment extends Fragment {

    private SignupViewModel mViewModel;
    private LoginViewModel loginViewModel;
    EditText firstNameET, secondNameET, phoneET, emailET, passwordET, passwordAgainET;
    Button createAccountBtn;


    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSignupBinding binding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_signup, container, false);
        firstNameET = binding.firstNameET;
        secondNameET = binding.secondNameET;
        phoneET = binding.phoneET;
        emailET = binding.emailET;
        passwordET = binding.passwordET;
        passwordAgainET = binding.passwordAgainET;
        createAccountBtn = binding.createAccountBtn;

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean itemCheck = checksignUp(
                        firstNameET.getText().toString(),
                        secondNameET.getText().toString(),
                        phoneET.getText().toString(),
                        emailET.getText().toString(),
                        passwordET.getText().toString(),
                        passwordAgainET.getText().toString()
                );

                mViewModel.createNewAccount(
                        firstNameET.getText().toString(),
                        secondNameET.getText().toString(),
                        phoneET.getText().toString(),
                        emailET.getText().toString(),
                        passwordET.getText().toString()
                );
            }
        });

        View view = binding.getRoot();
        return view;
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


    private boolean checksignUp(String firsName, String secondName,
                                String phone, String email,
                                String password, String passwordAgain) {
        int error = 0;
        if (email.equals("") || null==email || !checkValidEmail(email) ){
            emailET.setError("Please Enter Valid Email Address");
            error++;
        }

        // Password
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

        // Password again
        if (passwordAgain.equals("") || null == password){
            passwordAgainET.setError("Please Enter Valid Password");
            error++;
        }


        if (passwordAgain.length() > 0 && password.length() < 6 ){
            passwordAgainET.setError("Password length must be between 6 to 10 character");
            error++;
        }

        if (passwordAgain.length() > 10 ){
            passwordAgainET.setError("Password length must be between 6 to 10 character");
            error++;
        }

        if (!password.equals(passwordAgain)){
            passwordET.setError("Password and password again must be the same");
            passwordAgainET.setError("Password and password again must be the same");
            error++;
        }



        // First Name
        if (firsName.equals("")){
            firstNameET.setError("Please Enter Valid Name");
            error++;
        }

        if (firsName.length() > 0 && firsName.length() < 2 ){
            firstNameET.setError("First Name length must be between 2 to 20 character");
            error++;
        }

        if (firsName.length() > 20 ){
            firstNameET.setError("First Name length must be between 6 to 20 character");
            error++;
        }

        // Second Name
        if (secondName.equals("")){
            secondNameET.setError("Please Enter Valid Name");
            error++;
        }

        if (secondName.length() > 0 && secondName.length() < 2 ){
            secondNameET.setError("Second Name length must be between 2 to 20 character");
            error++;
        }

        if (secondName.length() > 20 ){
            secondNameET.setError("Second Name length must be between 2 to 20 character");
            error++;
        }

        // Second Name
        if (phone.equals("")){
            phoneET.setError("Please Enter Valid Phone");
            error++;
        }

        if (phone.length() > 0 && phone.length() < 10 ){
            phoneET.setError("Phone length must be between 10 to 13 character");
            error++;
        }

        if (phone.length() > 13 ){
            phoneET.setError("Phone length must be between 10 to 13 character");
            error++;
        }






        if (error == 0){
            return true;
        }else {
            return false;
        }

    }
}
