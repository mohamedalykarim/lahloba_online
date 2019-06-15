package online.lahloba.www.lahloba.ui.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.RowFavoriteItemBinding;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    List<ProductItem> productItems;

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowFavoriteItemBinding binding = RowFavoriteItemBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                viewGroup,
                false
        );



        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int i) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(productItems.get(i).getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            Picasso.get().load(task.getResult())
                                    .placeholder(R.drawable.progress_animation)
                                    .into(holder.binding.image);


                        }
                    }
                });



            holder.binding.setProduct(productItems.get(i));



    }

    @Override
    public int getItemCount() {
        return productItems==null ? 0:productItems.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final RowFavoriteItemBinding binding;

        public FavoriteViewHolder(RowFavoriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }
}
