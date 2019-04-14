package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.ui.products.ProductsActivity;
import online.lahloba.www.lahloba.ui.sub_menu.SubMenuActivity;
import online.lahloba.www.lahloba.utils.Constants;

public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.SubMenuViewHolder> {
    List<SubMenuItem> subMenuItems;
    Context context;

    public SubMenuAdapter(Context context) {
        this.context = context;
    }

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
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subMenuItem.isHasChild()){
                    Intent intent = new Intent(context, SubMenuActivity.class);
                    intent.putExtra(Constants.EXTRA_SUBTITLE_ID, subMenuItem.getId());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra(Constants.EXTRA_SUBTITLE_ID, subMenuItem.getId());
                    context.startActivity(intent);
                }
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
