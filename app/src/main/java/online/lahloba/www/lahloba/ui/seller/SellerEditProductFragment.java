package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.SellerEditProductFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.CustomSpinnerAdapter;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;


public class SellerEditProductFragment extends Fragment {
    String productId;
    SellerEditProductFragmentBinding binding;

    ProductItem enProductItem;

    private SellerEditProductViewModel mViewModel;
    private List<String> markets;
    private boolean imagePicked;

    public static SellerEditProductFragment newInstance() {
        return new SellerEditProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SellerEditProductFragmentBinding
                .inflate(inflater, container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerEditProductViewModel.class);



        mViewModel.startGetProductForEdit(productId);
        mViewModel.startGetMarketPlacesForSeller();

        enProductItem = null;

        binding.setViewModel(mViewModel);


        binding.fImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(getActivity()) // Activity or Fragment
                        .single()
                        .start();
            }
        });



        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fStepInvalidate();
                if (mViewModel.helper.getError() == 0){
                    addProduct();
                    Toast.makeText(getContext(), "Edit product is ongoing", Toast.LENGTH_SHORT).show();
                    ((Activity)binding.getRoot().getContext()).finish();
                }
            }
        });




        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();



        mViewModel.getArProductItemForEdit().observe(this, productItem -> {
            if (null == productItem) return;
            mViewModel.helper.setArProduct(productItem);

        });


        mViewModel.getEnProductItemForEdit().observe(this, productItem -> {
            if (null == productItem) return;
            this.enProductItem = productItem;
            mViewModel.helper.setEnProduct(productItem);

            if (!imagePicked){
                loadOldImage(enProductItem.getImage());
            }

        });


        mViewModel.getMarketPlacesForSeller().observe(this, marketPlaces -> {
            if (marketPlaces == null) return;




            markets = new ArrayList<>();

            markets.clear();


            ArrayList<String> marketsName = new ArrayList<>();

            for (int i = 0; i < marketPlaces.size(); i++)
            {
                marketsName.add(marketPlaces.get(i).getName());
                markets.add(marketPlaces.get(i).getId());
            }


            CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item,marketsName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner2.setAdapter(adapter);


            if (enProductItem == null)return;

            for (int i = 0; i < markets.size(); i++){
                if (markets.get(i).equals(enProductItem.getMarketPlaceId())){
                    binding.spinner2.setSelection(i);

                }
            }


        });



    }

    private void addProduct() {

        ProductItem arProductItem = new ProductItem();
        ProductItem enProductItem = new ProductItem();

        arProductItem.setId(this.enProductItem.getId());
        arProductItem.setStatus(binding.switch2.isChecked());
        arProductItem.setPrice(binding.priceET.getText().toString());
        arProductItem.setTitle(binding.arabicNameET.getText().toString());
        arProductItem.setMarketPlaceId(markets.get(binding.spinner2.getSelectedItemPosition()));
        arProductItem.setParentId(this.enProductItem.getParentId());
        arProductItem.setParentIdMarketPlaceId( this.enProductItem.getParentId()+ "-" + markets.get(binding.spinner2.getSelectedItemPosition()));
        arProductItem.setParentIdSellerId(this.enProductItem.getParentId() +"-"+ FirebaseAuth.getInstance().getUid());
        arProductItem.setSellerId(FirebaseAuth.getInstance().getUid());
        arProductItem.setDescription(binding.arabicDescET.getText().toString());



        enProductItem.setId(this.enProductItem.getId());
        enProductItem.setStatus(binding.switch2.isChecked());
        enProductItem.setPrice(binding.priceET.getText().toString());
        enProductItem.setTitle(binding.englishNameET.getText().toString());
        enProductItem.setMarketPlaceId(markets.get(binding.spinner2.getSelectedItemPosition()));
        enProductItem.setParentId(this.enProductItem.getParentId());
        enProductItem.setParentIdMarketPlaceId( this.enProductItem.getParentId()+ "-" + markets.get(binding.spinner2.getSelectedItemPosition()));
        enProductItem.setParentIdSellerId(this.enProductItem.getParentId() +"-"+ FirebaseAuth.getInstance().getUid());
        enProductItem.setSellerId(FirebaseAuth.getInstance().getUid());
        enProductItem.setDescription(binding.englishDescET.getText().toString());



        mViewModel.startAddNewProduct(mViewModel.helper.getBitmap(), enProductItem, arProductItem);


    }

    private void fStepInvalidate() {
        mViewModel.helper.setError(0);

        if (binding.englishNameET.getText().toString().equals("") || binding.englishNameET.getText().toString().length() < 6){
            binding.englishNameET.setError("Product English name must be at least 6 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }

        if (binding.arabicNameET.getText().toString().equals("") || binding.arabicNameET.getText().toString().length() < 6){
            binding.arabicNameET.setError("Product Arabic name must be at least 6 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }



        if (binding.priceET.getText().toString().equals("0") || binding.priceET.getText().toString().length() < 1){
            binding.priceET.setError("Product Price must be at least 1 character");
            mViewModel.helper.setError(mViewModel.helper.getError() + 1);
        }




    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void reset(){
        mViewModel.resetEditPage();
    }

    public void pickImage(Image image){
        imagePicked = true;
        Bitmap myBitmap = BitmapFactory.decodeFile(image.getPath());

        myBitmap = Utils.getResizedBitmap(myBitmap, 200);

        binding.fImage.setImageBitmap(myBitmap);
        mViewModel.helper.setBitmap(myBitmap);

    }


    private void loadOldImage(String oldImageUrl){
        if (oldImageUrl == null)return;

        /**
         * Load image from the storage
         */

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(oldImageUrl)
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get()
                                    .load(task.getResult())
                                    .placeholder(R.drawable.progress_animation)
                                    .into(binding.fImage);
                        }
                    }

                });



    }
}
