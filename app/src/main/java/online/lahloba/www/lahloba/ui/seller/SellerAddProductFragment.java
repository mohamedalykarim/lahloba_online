package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.SellerAddProductFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.SellerAddProductSpinnerAdapter;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;


public class SellerAddProductFragment extends Fragment {
    SellerAddProductFragmentBinding binding;

    private SellerAddProductViewModel mViewModel;
    private HashMap<String, String> markets;


    public static SellerAddProductFragment newInstance() {
        return new SellerAddProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SellerAddProductFragmentBinding.inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerAddProductViewModel.class);

        binding.setViewModel(mViewModel);
        binding.toolbar.setTitle("Add new Product");
        binding.toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        ((SellerAddProductActivity)getActivity()).setSupportActionBar(binding.toolbar);

        markets = new HashMap<String,String>();


        binding.fImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(getActivity()) // Activity or Fragment
                        .single()
                        .start();
            }
        });


        mViewModel.startGetMarketPlacesForSeller();


        /**
         *  Finish adding product
         */

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStepInvalidate();

                if (mViewModel.helper.getError() == 0){
                    addProduct();
                    getActivity().finish();
                }



            }
        });


        /**
         *  Finish adding product and go to edit
         */

        binding.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStepInvalidate();

                if (mViewModel.helper.getError() == 0){
                    addProduct();
                    getActivity().finish();
                }


            }
        });




        return binding.getRoot();
    }

    private void addProduct() {
        String push = FirebaseDatabase.getInstance().getReference().child("Product/en").push().getKey();

        ProductItem arProductItem = new ProductItem();
        ProductItem enProductItem = new ProductItem();

        arProductItem.setId(push);
        arProductItem.setStatus(binding.switch1.isChecked());
        arProductItem.setPrice(binding.fPriceET.getText().toString());
        arProductItem.setTitle(binding.fArabicNameET.getText().toString());
        arProductItem.setMarketPlaceId(markets.get(binding.spinner.getSelectedItem().toString()));
        arProductItem.setParentId(mViewModel.helper.getCategoryId());
        enProductItem.setParentIdMarketPlaceId( mViewModel.helper.getCategoryId()+ "-" + markets.get(binding.spinner.getSelectedItem().toString()));
        arProductItem.setParentIdSellerId(mViewModel.helper.getCategoryId() +"-"+ FirebaseAuth.getInstance().getUid());
        arProductItem.setSellerId(FirebaseAuth.getInstance().getUid());



        enProductItem.setId(push);
        enProductItem.setStatus(binding.switch1.isChecked());
        enProductItem.setPrice(binding.fPriceET.getText().toString());
        enProductItem.setTitle(binding.fEnglishNameET.getText().toString());
        enProductItem.setMarketPlaceId(markets.get(binding.spinner.getSelectedItem().toString()));
        enProductItem.setParentId(mViewModel.helper.getCategoryId());
        enProductItem.setParentIdMarketPlaceId( mViewModel.helper.getCategoryId()+ "-" + markets.get(binding.spinner.getSelectedItem().toString()));
        enProductItem.setParentIdSellerId(mViewModel.helper.getCategoryId() +"-"+ FirebaseAuth.getInstance().getUid());
        enProductItem.setSellerId(FirebaseAuth.getInstance().getUid());

        mViewModel.startAddNewProduct(mViewModel.helper.getMyBitmap(), enProductItem, arProductItem);


    }

    private void fStepInvalidate() {
        mViewModel.helper.setError(0);

        if (binding.fEnglishNameET.getText().toString().equals("") || binding.fEnglishNameET.getText().toString().length() < 6){
            binding.fEnglishNameET.setError("Product English name must be at least 6 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }

        if (binding.fArabicNameET.getText().toString().equals("") || binding.fArabicNameET.getText().toString().length() < 6){
            binding.fArabicNameET.setError("Product Arabic name must be at least 6 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }

        if (binding.fPriceET.getText().toString().equals("0") || binding.fPriceET.getText().toString().length() < 1){
            binding.fPriceET.setError("Product Price must be at least 1 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }

        if(mViewModel.helper.getMyBitmap() == null){
            Toast.makeText(getContext(), "Please Choose image before save", Toast.LENGTH_SHORT).show();
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);

        }

    }


    @Override
    public void onResume() {
        super.onResume();



        mViewModel.getMarketPlacesForSeller().observe(this, marketPlaces -> {
            if (marketPlaces == null) return;

            ArrayList<String> marketsName = new ArrayList<>();

            for (int i = 0; i < marketPlaces.size(); i++)
            {
                marketsName.add(marketPlaces.get(i).getName());
                markets.put(marketPlaces.get(i).getName(),marketPlaces.get(i).getId());
            }


            SellerAddProductSpinnerAdapter adapter = new SellerAddProductSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item,marketsName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(adapter);


        });
    }

    public void pickImage(Image image){
        Bitmap myBitmap = BitmapFactory.decodeFile(image.getPath());

        myBitmap = Utils.getResizedBitmap(myBitmap, 200);

        binding.fImage.setImageBitmap(myBitmap);
        mViewModel.helper.setMyBitmap(myBitmap);

    }

    public void setCategory(String categoryId) {
        mViewModel.helper.setCategoryId(categoryId);
    }
}
