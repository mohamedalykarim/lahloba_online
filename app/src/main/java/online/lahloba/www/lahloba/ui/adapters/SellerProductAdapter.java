package online.lahloba.www.lahloba.ui.adapters;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.SellerProductItemBinding;
import online.lahloba.www.lahloba.ui.seller.SellerProductsViewModel;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.SellerProductViewHolder> {
    List<ProductItem> productItems;
    SellerProductsViewModel viewModel;


    @NonNull
    @Override
    public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SellerProductItemBinding binding = SellerProductItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        return new SellerProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerProductViewHolder holder, int i) {
        ProductItem item = productItems.get(i);

        holder.binding.setItem(item);
        holder.binding.setViewModel(viewModel);


        /**
         * Load image from the storage
         */

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
                                    .placeholder(R.drawable.progress_animation)
                                    .into(holder.binding.productImage);
                        }
                    }
                });



        /**
         * Start of Handling Price Change
         */


        long delay = 1000; // 1 seconds after user stops typing
        final long[] last_text_edit = {0};
        Handler handler = new Handler();

        final String[] price = new String[1];

        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    viewModel.startChangeProductPrice(item.getId(), price[0]);
                }
            }
        };

        holder.binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                price[0] = s.toString();

                if (s.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }

            }
        });


        /**
         * End of Handling Price Change
         */



    }

    @Override
    public int getItemCount() {
        return productItems == null ? 0 : productItems.size();
    }

    class SellerProductViewHolder extends RecyclerView.ViewHolder{

        private final SellerProductItemBinding binding;

        public SellerProductViewHolder(SellerProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public void setViewModel(SellerProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
