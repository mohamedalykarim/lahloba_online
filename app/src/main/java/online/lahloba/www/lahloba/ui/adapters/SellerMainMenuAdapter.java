package online.lahloba.www.lahloba.ui.adapters;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.data.model.SellerMainMenuItem;
import online.lahloba.www.lahloba.databinding.RowSellerMainItemBinding;
import online.lahloba.www.lahloba.utils.Constants;

import static online.lahloba.www.lahloba.utils.Constants.PACKAGE_NAME;

public class SellerMainMenuAdapter extends RecyclerView.Adapter<SellerMainMenuAdapter.SellerMainMenuViewHolder> {
    List<SellerMainMenuItem> menuItemList;

    @NonNull
    @Override
    public SellerMainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowSellerMainItemBinding binding = RowSellerMainItemBinding.inflate(inflater,viewGroup,false);
        return new SellerMainMenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerMainMenuViewHolder holder, int i) {
        SellerMainMenuItem item = menuItemList.get(i);
        holder.binding.setItem(item);


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(
                        holder.binding.getRoot().getContext(),
                        PACKAGE_NAME + item.getUri()
                ));

                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    class SellerMainMenuViewHolder extends RecyclerView.ViewHolder {
        private final RowSellerMainItemBinding binding;

        public SellerMainMenuViewHolder(@NonNull RowSellerMainItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public void setMenuItemList(List<SellerMainMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
