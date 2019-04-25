package online.lahloba.www.lahloba.ui.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.RowProductListBinding;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<ProductItem> productItemList;

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowProductListBinding binding = RowProductListBinding.inflate(inflater,viewGroup, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        productItemList.get(i).setCount(0);
        ProductItem item = productItemList.get(i);
        productViewHolder.bind(item);
        productViewHolder.clicks(i, item);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(item.getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get()
                                    .load(task.getResult())
                                    .into(productViewHolder.binding.productImage);
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

        public void bind(ProductItem item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }

        public void clicks(int i, ProductItem item){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("cart")
                    .child("userId")
                    .child(item.getId()).child("count")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(null != dataSnapshot.getValue()){
                                productItemList.get(i).setCount(Integer.parseInt(dataSnapshot.getValue().toString()));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            binding.minusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productCount = productItemList.get(i).getCount();
                    if(productCount>0){

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("cart")
                                .child("userId")
                                .child(item.getId()).child("count")
                                .runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        Object count = mutableData.getValue();

                                        if(null == count ){
                                            return Transaction.success(mutableData);
                                        }else{
                                            int countInt = Integer.parseInt("0"+count);
                                            mutableData.setValue(countInt - 1);
                                            return Transaction.success(mutableData);
                                        }

                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                    }
                                });
                    }
                }
            });

            binding.plusContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productCount = productItemList.get(i).getCount();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("cart")
                            .child("userId")
                            .child(item.getId()).child("count")
                            .runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                    Object count = mutableData.getValue();

                                    if(null == count ){
                                        return Transaction.success(mutableData);
                                    }else {
                                        int countInt = Integer.parseInt("0"+count);
                                        mutableData.setValue(countInt + 1);

                                        return Transaction.success(mutableData);
                                    }

                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                }
                            });
                }
            });

            binding.addContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.plusMinusContainer.setVisibility(View.VISIBLE);
                    binding.addContainer.setVisibility(View.GONE);
                    int count = productItemList.get(i).getCount();
                    productItemList.get(i).setCount(count + 1);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("cart")
                            .child("userId")
                            .child(item.getId()).child("count")
                            .setValue(1);

                }
            });

        }
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }
}
