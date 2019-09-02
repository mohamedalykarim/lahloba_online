package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.databinding.FragmentProductBinding;
import online.lahloba.www.lahloba.ui.adapters.ProductAdapter;
import online.lahloba.www.lahloba.ui.cart.CartActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.products.bottom_sheet.ResetCartBottomSheet;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

public class ProductsFragment extends Fragment {
    FragmentProductBinding binding;
    private ProductsViewModel mViewModel;
    private LoginViewModel loginViewModel;
    List<ProductItem> productItemList;
    ProductAdapter productAdapter;
    String userId;
    ResetCartBottomSheet resetCartBottomSheet;
    private String subId;
    private boolean resetAlertApear;




    public static ProductsFragment newInstance(Bundle args) {
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_product, container, false);
        binding.setLifecycleOwner(this);

        resetCartBottomSheet = new ResetCartBottomSheet();


        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ProductsViewModel.class);

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext());
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);


        /**
         * Recycler view
         * adapter
         */

        productItemList = new ArrayList<>();

        productAdapter = new ProductAdapter(getContext());
        productAdapter.setProductItemList(productItemList);
        productAdapter.setmViewModel(mViewModel);

        binding.productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.productsRecyclerView.setAdapter(productAdapter);




        Bundle bundle = getArguments();
        subId = bundle.getString(Constants.EXTRA_SUBTITLE_ID);




        binding.setProductViewModel(mViewModel);

        mViewModel.deleteAllFromCartCount0();



        floatButton(container);








        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();


        mViewModel.startProductsForCategory(subId);



        loginViewModel.getCurrentUserDetails().observe(this,currentUser->{
            if (null == currentUser)return;

            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                userId = currentUser.getId();
                productAdapter.setUserId(userId);
                mViewModel.startGetCartItems(userId);

            }
        });




        mViewModel.getCartItems().observe(this, cartItems -> {
            if(cartItems == null)return;
            if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

            mViewModel.productVMHelper.setCartCount(cartItems.size());
        });

        mViewModel.getCartItemFromInternal().observe(this, cartItems->{
            if (cartItems == null) return;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) return;

            mViewModel.productVMHelper.setCartCount(cartItems.size());
        });



        mViewModel.getProductsForCategory().observe(this, products->{
            productItemList.clear();
            productItemList.addAll(products);
            productAdapter.notifyDataSetChanged();

        });


        productAdapter.getCartHasOLdFarProducts().observe(this, isOldFarProducts->{
            if (isOldFarProducts == null)return;

            if (isOldFarProducts){
                if (!resetAlertApear){

                    resetCartBottomSheet.show(getFragmentManager(),"resetCartBottomSheet");
                    resetCartBottomSheet.setCancelable(false);
                    resetAlertApear = true;
                }

            }
        });





    }


    /**
     * Cart Float item
     * @param container
     */

    public void floatButton(View container){
        binding.floatingToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), CartActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID,userId);
                container.getContext().startActivity(intent);
            }
        });
    }


    /**
     *  Reset Cart Items
     * @param id
     */


    public void onResetCartItemClicked(int id) {
        if (id== R.id.confirmBtn){
            mViewModel.startResetFirebaseCart();
            resetCartBottomSheet.dismiss();
            resetAlertApear = false;
        }else if (id == R.id.cancelBtn){
            resetCartBottomSheet.dismiss();
            resetAlertApear = false;
        }
    }
}
