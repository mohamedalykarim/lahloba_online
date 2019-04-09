package online.lahloba.www.lahloba.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.ui.sub_menu.SubMenuActivity;
import online.lahloba.www.lahloba.utils.Constants;

public class ShoppingAdapter extends BaseAdapter {
    List<MainMenuItem> shoppingItemList;


    @Override
    public int getCount() {
        return null==shoppingItemList ? 0 : shoppingItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.main_grid, null, false);
        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText(shoppingItemList.get(position).getTitle());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(shoppingItemList.get(position).getImage())
                .getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Picasso.get().load(task.getResult()).into(imageView);
                        }
                    }
                });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), SubMenuActivity.class);
                intent.putExtra(Constants.EXTRA_SUBTITLE_ID, shoppingItemList.get(position).getId());
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }

    public void setShoppingItemList(List<MainMenuItem> shoppingItemList) {
        this.shoppingItemList = shoppingItemList;
    }
}
