package online.lahloba.www.lahloba.ui.adapters;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
    public void onBindViewHolder(@NonNull SellerProductViewHolder holder, int position) {


        holder.binding.setItem(productItems.get(position));
        holder.binding.setViewModel(viewModel);


        /**
         * Load image from the storage
         */

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(productItems.get(position).getImage())
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



        String[] oldPrice = {productItems.get(position).getPrice()};
        boolean[] oldStatus = {productItems.get(position).isStatus()};


        holder.binding.editText.setTag(position);


        holder.binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                int positionTag = (int) holder.binding.editText.getTag();


                if (s.length() > 0) {

                    if (oldStatus[0] == holder.binding.enableSwitch.isChecked()
                            && holder.binding.editText.getText().toString().equals(oldPrice[0])){

                        productItems.get(positionTag).setWantSaveEdit(false);


                    }else if (oldStatus[0] == holder.binding.enableSwitch.isChecked()
                            && !holder.binding.editText.getText().toString().equals(oldPrice[0])){
                        productItems.get(positionTag).setWantSaveEdit(true);



                    }else if (oldStatus[0] != holder.binding.enableSwitch.isChecked()){
                        productItems.get(positionTag).setWantSaveEdit(true);

                    }



                } else {

                }


            }
        });


       holder.binding.enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (oldStatus[0] == holder.binding.enableSwitch.isChecked()
                        && holder.binding.editText.getText().toString().equals(oldPrice[0])){
                    productItems.get(position).setWantSaveEdit(false);
                    productItems.get(position).setStatus(isChecked);


                }else if (oldStatus[0] != holder.binding.enableSwitch.isChecked()
                        && holder.binding.editText.getText().toString().equals(oldPrice[0])){
                    productItems.get(position).setWantSaveEdit(true);
                    productItems.get(position).setStatus(isChecked);

                }else if (!holder.binding.editText.getText().toString().equals(oldPrice[0])){
                    productItems.get(position).setWantSaveEdit(true);
                    productItems.get(position).setStatus(isChecked);
                }




            }
        });



       holder.binding.updateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               viewModel.startChangeProductPrice(productItems.get(position).getId(), holder.binding.editText.getText().toString());
               viewModel.startChangeProductStatus(productItems.get(position).getId(), holder.binding.enableSwitch.isChecked());
               oldPrice[0] = productItems.get(position).getPrice();
               oldStatus[0] = productItems.get(position).isStatus();
               productItems.get(position).setWantSaveEdit(false);


               notifyDataSetChanged();

           }
       });


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
