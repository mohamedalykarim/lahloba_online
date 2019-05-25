package online.lahloba.www.lahloba.ui.cart;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.fragments.CartFragment;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked {

    LogginBottomSheet logginBottomSheet;
    ShippingMethodBottomSheet shippingMethodBottomSheet;

    private CartViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);

        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){
                VMPHelper vmpHelper = new VMPHelper();
                vmpHelper.setUserId(intent.getStringExtra(EXTRA_USER_ID));
                ViewModelProviderFactory factory = Injector.getVMFactory(this, vmpHelper);
                mViewModel = ViewModelProviders.of(this, factory).get(CartViewModel.class);


                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CartFragment.newInstance(bundle))
                        .commitNow();


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            /**
                             * If not login ask user for login
                             */
                            
                            logginBottomSheet = new LogginBottomSheet();
                            logginBottomSheet.show(getSupportFragmentManager(),"");
                        }else{
                            /**
                             * user is log in
                             */


                            if (mViewModel.cartVMHelper.getShippingMethodSelected() == null){
                                /**
                                 * Shipping method not set
                                 */

                                shippingMethodBottomSheet = new ShippingMethodBottomSheet();
                                shippingMethodBottomSheet.show(getSupportFragmentManager(),"");

                            }else{
                                /**
                                 * Shipping method is set
                                 */

                                Toast.makeText(CartActivity.this, ""+mViewModel.cartVMHelper.getShippingMethodSelected(), Toast.LENGTH_SHORT).show();


                            }
                            
                            

                        }
                    }
                });
            }



        }
    }


    @Override
    public void onLoginSheetItemClicked(int id) {
        if (id == R.id.loginBtn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            logginBottomSheet.dismiss();
        }
    }

    @Override
    public void onShippingMethodClicked(int id) {
        if (id == R.id.freeShippingBtn){
            mViewModel.cartVMHelper.setShippingMethodSelected(CartVMHelper.FREE_SHIPPING);
            shippingMethodBottomSheet.dismiss();
        }else if (id == R.id.hyperLocalShippingBtn3){
            mViewModel.cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);
            shippingMethodBottomSheet.dismiss();
        }
    }
}
