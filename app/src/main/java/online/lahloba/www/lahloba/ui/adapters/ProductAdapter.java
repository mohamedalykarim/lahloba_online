package online.lahloba.www.lahloba.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.ProductItem;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<ProductItem> productItemList;

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_product_list, viewGroup, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return null == productItemList? 0 : productItemList.size() ;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
    }
}
