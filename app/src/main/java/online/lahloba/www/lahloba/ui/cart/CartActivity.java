package online.lahloba.www.lahloba.ui.cart;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.databinding.CartActivityBinding;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddOrderConfirmBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.AddressBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.LogginBottomSheet;
import online.lahloba.www.lahloba.ui.cart.bottom_sheet.ShippingMethodBottomSheet;
import online.lahloba.www.lahloba.ui.login.LoginActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.OrderStatusUtils;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;

public class CartActivity extends AppCompatActivity
implements
        LogginBottomSheet.OnLoginSheetClicked,
        ShippingMethodBottomSheet.OnShippingMethodClicked,
        CartFragment.AddressesToActivityFromCart,
        AddressBottomSheet.SendAddressToCart,
        AddOrderConfirmBottomSheet.ConfirmClickListener

{

    LogginBottomSheet logginBottomSheet;
    ShippingMethodBottomSheet shippingMethodBottomSheet;
    AddressBottomSheet addressBottomSheet;
    AddOrderConfirmBottomSheet addOrderConfirmBottomSheet;

    private LoginViewModel loginViewModel;
    private boolean isStartAddingNewOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.cart_activity);
        binding.setLifecycleOwner(this);

        logginBottomSheet = new LogginBottomSheet();
        shippingMethodBottomSheet = new ShippingMethodBottomSheet();
        addressBottomSheet = new AddressBottomSheet();


        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this,null);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);


        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(null != intent && intent.hasExtra(EXTRA_USER_ID)){

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_USER_ID, intent.getStringExtra(EXTRA_USER_ID));
                CartFragment cartFragment = CartFragment.newInstance(bundle, this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, cartFragment)
                        .commitNow();


                binding.cartContinueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == FirebaseAuth.getInstance().getCurrentUser()){
                            /**
                             * If not login ask user for login
                             */
                            
                            logginBottomSheet.show(getSupportFragmentManager(),"");
                        }else{
                            /**
                             * user is log in
                             */



                            if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                    .getmViewModel().cartVMHelper.getAddressSelected() == null){

                                addressBottomSheet.show(getSupportFragmentManager(),"");

                            }else {

                                if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                                    Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                  /*
                                   * address selected then choose shipping method
                                   */

                                if (((CartFragment)getSupportFragmentManager().getFragments().get(0))
                                        .getmViewModel().cartVMHelper.getShippingMethodSelected() == null){
                                    /*
                                     * Shipping method not set
                                     */

                                    shippingMethodBottomSheet.show(getSupportFragmentManager(),"");

                                }else{

                                    if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                                        Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    /*
                                     * Start the order
                                     */

                                    addOrderConfirmBottomSheet = new AddOrderConfirmBottomSheet();
                                    addOrderConfirmBottomSheet.show(getSupportFragmentManager(),"");

                                }
                            }



                            
                            

                        }
                    }
                });
            }



        }
    }

    @Override
    protected void onResume() {
        super.onResume();


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
            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel()
                    .cartVMHelper.setShippingMethodSelected(CartVMHelper.FREE_SHIPPING);
            shippingMethodBottomSheet.dismiss();
        }else if (id == R.id.hyperLocalShippingBtn3){
            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel().cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

            ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                    .getmViewModel().cartVMHelper
                    .setHyperlocalCost(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getNonFinalHyperLocalCost());

            shippingMethodBottomSheet.dismiss();
        }
    }



    @Override
    public void onAddressesToActivityFromCart(List<AddressItem> addresses) {
        if (addresses!=null){
            addressBottomSheet.setAddresses(addresses);
        }
    }



    /**
     * Get selected Address From Adapter to activity
     */
    @Override
    public void onSendAddressToCart(AddressItem addressItems) {
        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper.setAddressSelected(addressItems);

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .startRetrieveCartItemsAfterSelectAddress();

        if (addressBottomSheet !=null)
            addressBottomSheet.dismiss();



        /**
         *   Choose hyperlocal automatically
         *
         *
         */

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper.setShippingMethodSelected(CartVMHelper.HYPERLOCAL_SHIPPING);

        ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                .getmViewModel().cartVMHelper
                .setHyperlocalCost(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getNonFinalHyperLocalCost());


    }

    @Override
    public void onClickConfirmItem(int id) {
        if (id== R.id.confirmBtn){
            /**
             * Confirm adding order
             */


            if (!isStartAddingNewOrder){
                if (((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList().size() < 1){
                    Toast.makeText(CartActivity.this, "Add products first", Toast.LENGTH_SHORT).show();
                    return;
                }


                OrderItem orderItem = new OrderItem();
                HashMap<String, CartItem> products = new HashMap<>();
                for (CartItem item : ((CartFragment)getSupportFragmentManager().getFragments().get(0)).getCartItemList()){
                    products.put(item.getId(), item);
                }


                orderItem.setProducts(products);
                orderItem.setAddressSelected(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getAddressSelected());
                orderItem.setHyperlocalCost(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getHyperlocalCost());
                orderItem.setTotal(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getTotal());
                orderItem.setPay_method(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getPay_method());
                orderItem.setShippingMethodSelected(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getShippingMethodSelected());
                orderItem.setOrderTotal(((CartFragment)getSupportFragmentManager().getFragments().get(0)).getmViewModel().cartVMHelper.getTotal()
                        +((CartFragment)getSupportFragmentManager().getFragments().get(0))
                        .getmViewModel().cartVMHelper.getHyperlocalCost()
                );

                Date now = new Date();
                orderItem.setDate(now);

                orderItem.setOrderStatus(OrderStatusUtils.ORDER_STATUS_PENDING);

                ((CartFragment)getSupportFragmentManager().getFragments().get(0))
                        .getmViewModel()
                        .startNewOrder(orderItem);

                isStartAddingNewOrder = true;


            }



        }else if (id == R.id.cancelBtn){
            addOrderConfirmBottomSheet.dismiss();
        }
    }
}
