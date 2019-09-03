package online.lahloba.www.lahloba.ui.adapters;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.RowProductListBinding;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    List<ProductItem> productItemList;
    String userId ="userId";
    MutableLiveData<Boolean> cartHasOLdFarProducts;
    private ProductsViewModel mViewModel;


    public ProductAdapter(Context context) {
        this.context = context;
        cartHasOLdFarProducts = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowProductListBinding binding = RowProductListBinding.inflate(inflater,viewGroup, false);

        if (FirebaseAuth.getInstance().getUid() != null){
            userId = FirebaseAuth.getInstance().getUid();
        }

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.binding.setItem(productItemList.get(position));

        /**
         * Read cart item
         */

        if (FirebaseAuth.getInstance().getUid() == null){

            mViewModel.getSpecificCartItemFromInternal(productItemList.get(position).getId())
                    .observe((LifecycleOwner) context, item -> {
                        if (item == null) return;
                            holder.binding.setCartItem(item);
                    });
        }else{


            mViewModel.startGetCartItemById(productItemList.get(position).getId());

        }





        /**
         * add the item
         */


        holder.binding.addContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getUid() == null){

                    CartItem cartItem = new CartItem();
                    cartItem.setCount(1);
                    cartItem.setProductId(productItemList.get(position).getId());
                    cartItem.setImage(productItemList.get(position).getImage());
                    cartItem.setPrice(productItemList.get(position).getPrice());
                    cartItem.setProductName(productItemList.get(position).getTitle());
                    cartItem.setCurrency("EGP");
                    cartItem.setMarketId(productItemList.get(position).getMarketPlaceId());

                    mViewModel.insertCartItemToInternaldb(cartItem);




                }else {



                    mViewModel.startAddProductToFirebaseCart(productItemList.get(position));

                }



            }
        });


        /**
         * add to count
         */

        holder.binding.plusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null){
                    mViewModel.addToCartItemCountInternaldb(productItemList.get(position).getId());


                }else{
                    mViewModel.startAddToCartProductCount(productItemList.get(position).getId());
                }
            }
        });



            /**
         * remove from count
         */

        holder.binding.minusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null){

                    mViewModel.removeFromCartItemCountInternaldb(productItemList.get(position).getId());

                }else{
                    mViewModel.startRemoveFromCartProductCount(productItemList.get(position).getId());
                }
            }
        });









        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child(productItemList.get(position).getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get()
                                    .load(task.getResult())
                                    .placeholder(R.drawable.progress_animation)
                                    .into(holder.binding.productImage);
                        }
                    }
                });




        mViewModel.getCartItem().observe((LifecycleOwner) context, cartItem -> {
            if (cartItem == null)return;

            if (cartItem.getId().equals(productItemList.get(position).getId())){
                holder.binding.setCartItem(cartItem);
            }
        });




    }

    private void favorites(@NonNull ProductViewHolder holder, int i, ProductItem item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Favorites")
                .child(userId)
                .child(item.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            Picasso.get().load(R.drawable.favorite_icon).into(holder.binding.favoriteImage);
                            productItemList.get(i).setFavorite(true);
                        }else{
                            Picasso.get().load(R.drawable.no_favorite_icon).into(holder.binding.favoriteImage);
                            productItemList.get(i).setFavorite(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
        });

        holder.binding.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productItemList.get(i).isFavorite()){
                    databaseReference.child("Favorites")
                            .child(userId)
                            .child(item.getId()).removeValue();
                }else {
                    databaseReference.child("Favorites")
                            .child(userId)
                            .child(item.getId()).setValue(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == productItemList? 0 : productItemList.size() ;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        RowProductListBinding binding;
        public ProductViewHolder(RowProductListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }



    public void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public MutableLiveData<Boolean> getCartHasOLdFarProducts() {
        return cartHasOLdFarProducts;
    }

    public void setmViewModel(ProductsViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
