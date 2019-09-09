package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.FragmentProductDetailsBinding;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

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

    }


    public void setProductId(String productId) {
        this.productId = productId;
    }
}
