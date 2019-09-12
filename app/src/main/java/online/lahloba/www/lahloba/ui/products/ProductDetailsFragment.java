package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductOption;
import online.lahloba.www.lahloba.databinding.FragmentProductDetailsBinding;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.Utils;
import online.lahloba.www.lahloba.utils.comparator.ProductOptionComperatorByValue;

public class ProductDetailsFragment extends Fragment {
    private ProductDetailsViewModel mViewModel;
    private FragmentProductDetailsBinding binding;
    String productId;

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ProductDetailsViewModel.class);



        mViewModel.startGetProductItem(productId);
        mViewModel.startGetCartItemForProduct(productId);
        mViewModel.startGetFavoriteItem(productId);
        mViewModel.startGetProductOptions(productId);



        binding.addContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startAddItemToCart(mViewModel.helper.getProductItem());
            }
        });


        binding.plusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startAddtoCartCount(productId);
            }
        });

        binding.minusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startRemoveFromCartCount(productId);
            }
        });


        binding.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.startChangeFavoriteStatus(productId);
            }
        });




        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();


        mViewModel.getProduct().observe((LifecycleOwner) getContext(), productItem -> {
            if (productItem == null)return;

            if (productId.equals(productItem.getId())){
                mViewModel.helper.setProductItem(productItem);
                binding.setProduct(productItem);
                mViewModel.startGetMarketPlace(productItem.getMarketPlaceId());


                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef.child(productItem.getImage())
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Picasso.get()
                                            .load(task.getResult())
                                            .placeholder(R.drawable.progress_animation)
                                            .into(binding.thumb, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    binding.thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                }

                                                @Override
                                                public void onError(Exception e) {

                                                }
                                            });
                                }
                            }
                        });
            }

        });

        mViewModel.getMarketPlace().observe((LifecycleOwner) getContext(), marketPlace -> {
            if (marketPlace == null)return;
            if (mViewModel.helper.getProductItem() == null)return;

            if (mViewModel.helper.getProductItem().getMarketPlaceId().equals(marketPlace.getId())){
                mViewModel.helper.setMarketPlace(marketPlace);
                binding.setMarketPlaceItem(marketPlace);
                mViewModel.startGetSellerDetails(marketPlace.getSellerId());
            }
        });

        mViewModel.getUserDetails().observe((LifecycleOwner) getContext(), userItem -> {
            if (userItem == null)return;
            if (mViewModel.helper.getMarketPlace() == null)return;

            if (mViewModel.helper.getMarketPlace().getSellerId().equals(userItem.getId())){
                binding.setSellerDetails(userItem);
            }

        });

        mViewModel.getCartItem().observe((LifecycleOwner) getContext(), cartItem -> {
            if (cartItem == null)return;

            if (cartItem.getId().equals(productId)){
                mViewModel.helper.setCartItem(cartItem);
                binding.setCartItem(cartItem);
            }
        });

        mViewModel.getFavoritesItem().observe((LifecycleOwner) getContext(), favoriteItem -> {
            if (favoriteItem == null);

            binding.setFavoriteItem(favoriteItem);
            mViewModel.helper.setFavoriteItem(favoriteItem);
        });


        mViewModel.getProductOptions().observe((LifecycleOwner) getContext(), productOptions->{
            if (productOptions == null){
                binding.optionsContainer.setVisibility(View.GONE);
                return;
            }
            binding.optionsContainer.removeAllViews();



            for (DataSnapshot option : productOptions.getChildren()){

                Spinner spinner = new Spinner(getContext());
                spinner.setTag(option.getKey());


                HashMap<String, String> optionItems = new HashMap<>();

                for (DataSnapshot optionNode : option.getChildren()){
                    optionItems.put(
                            optionNode.getKey(),
                            optionNode.getValue().toString()
                    );
                }

                ProductOptionComperatorByValue comperatorByValue = new ProductOptionComperatorByValue(optionItems);
                TreeMap<String, String> sortedOptionItems = new TreeMap<String, String>(comperatorByValue);
                sortedOptionItems.putAll(optionItems);

                List<String> titles = new ArrayList<>();
                List<String> keys = new ArrayList<>();
                List<String> values = new ArrayList<>();
                Iterator it = sortedOptionItems.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry pair = (Map.Entry)it.next();
                    titles.add(pair.getKey().toString() + " | " + pair.getValue().toString() + " " +"EGP");
                    keys.add(pair.getKey().toString());
                    values.add(pair.getValue().toString());
                    it.remove();
                }



                spinner.setBackgroundResource(R.drawable.btn_bg_2);
                spinner.setPaddingRelative(5,5,5,5);

                spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, titles));
                binding.optionsContainer.addView(spinner);

                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) spinner.getLayoutParams();
                marginLayoutParams.setMargins(50,10,50,0);



                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (mViewModel.helper.getCartItem() == null)return;
                        ProductOption productOption = new ProductOption();
                        productOption.setOptionId(spinner.getTag().toString());
                        productOption.setOptionKey(keys.get(position));
                        productOption.setOptionValue(values.get(position));
                        mViewModel.startAddOptionToCartItem(productId, productOption);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

        });



    }


    public void setProductId(String productId) {
        this.productId = productId;
    }
}
