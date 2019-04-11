package online.lahloba.www.lahloba.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.SubMenuItem;

public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.SubMenuViewHolder> {
    List<SubMenuItem> subMenuItems;

    @NonNull
    @Override
    public SubMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_sub_menu, viewGroup,false);
        return new SubMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubMenuViewHolder holder, int i) {
        SubMenuItem subMenuItem = subMenuItems.get(i);
        holder.titleTV.setText(subMenuItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return (null== subMenuItems? 0 : subMenuItems.size());
    }

    class SubMenuViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleTV;
        public SubMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
            titleTV = itemView.findViewById(R.id.titleTV);
        }
    }

    public void setSubMenuItems(List<SubMenuItem> subMenuItems) {
        this.subMenuItems = subMenuItems;
    }
}
