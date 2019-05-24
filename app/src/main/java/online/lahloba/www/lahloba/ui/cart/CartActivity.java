package online.lahloba.www.lahloba.ui.cart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.viewholder.LoginMessageViewHolder;
import online.lahloba.www.lahloba.ui.fragments.CartFragment;
import online.lahloba.www.lahloba.utils.Constants;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);


        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CartFragment.newInstance(bundle))
                        .commitNow();


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            DialogPlus dialogPlus = DialogPlus.newDialog(CartActivity.this)
                                    .setExpanded(true)
                                    .setContentHolder(new LoginMessageViewHolder(R.layout.dialog_plus_header))
                                    .create();

                            dialogPlus.show();

                        }else{

                        }
                    }
                });
            }



        }
    }
}
