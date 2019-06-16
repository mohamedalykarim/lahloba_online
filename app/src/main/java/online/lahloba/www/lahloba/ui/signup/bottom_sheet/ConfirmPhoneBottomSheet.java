package online.lahloba.www.lahloba.ui.signup.bottom_sheet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import online.lahloba.www.lahloba.databinding.BottomSheetConfirmPhoneBinding;
import online.lahloba.www.lahloba.ui.signup.SignupViewModel;

public class ConfirmPhoneBottomSheet extends BottomSheetDialogFragment {
    SignupViewModel signupViewModel;
    String firstName, lastName, phone, email, password;
    FirebaseAuth auth;

    String codeSent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetConfirmPhoneBinding binding = BottomSheetConfirmPhoneBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        
        binding.sendVerificationCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phone);
            }
        });
        
        
        binding.confirmPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verficationCode = binding.verificationCodeTv.toString();

                confirmPhone(verficationCode);
            }
        });
        
        
        
        return binding.getRoot();
    }

    private void confirmPhone(String verificationCode) {
        if (verificationCode == null) return;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,verificationCode);
        signInWithPhoneAuthCredential(credential);

    }

    private void sendVerificationCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            signupViewModel.createNewAccount(
                                    firstName,
                                    lastName,
                                    phone,
                                    email,
                                    password
                            );

                            dismiss();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                confirmPhone(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };


    public void setSignupViewModel(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
    }


    public void setData(String firstName, String lastName, String phone, String email, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}
