package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.databinding.RowSubMenuBinding;
import online.lahloba.www.lahloba.ui.seller.SellerProductsActivity;
import online.lahloba.www.lahloba.utils.Constants;

public class SubMenuChooseAdapter extends RecyclerView.Adapter<SubMenuChooseAdapter.SubMenuViewHolder> {
    List<SubMenuItem> subMenuItems;
    Context context;

    public SubMenuChooseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SubMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowSubMenuBinding rowSubMenuBinding = RowSubMenuBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                null,
                false
        );
        return new SubMenuViewHolder(rowSubMenuBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SubMenuViewHolder holder, int i) {
        SubMenuItem subMenuItem = subMenuItems.get(i);
        holder.titleTV.setText(subMenuItem.getTitle());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        if (subMenuItem.getImage() != null){

            storageRef.child(subMenuItem.getImage())
                    .getDownloadUrl()
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Picasso.get()
                                        .load(task.getResult())
                                        .placeholder(R.drawable.progress_animation)
                                        .into(holder.imageView);
                            }
                        }
                    });
        }


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SellerProductsActivity.class);
                intent.putExtra(Constants.EXTRA_SUBTITLE_ID, subMenuItem.getId());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null== subMenuItems? 0 : subMenuItems.size());
    }

    class SubMenuViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleTV;
        ConstraintLayout container;
        public SubMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
            titleTV = itemView.findViewById(R.id.titleTV);
            container = itemView.findViewById(R.id.row_sub);
        }
    }

    public void setSubMenuItems(List<SubMenuItem> subMenuItems) {
        this.subMenuItems = subMenuItems;
    }

    public void clearSubMenuItems() {
        this.subMenuItems.clear();
    }
}
