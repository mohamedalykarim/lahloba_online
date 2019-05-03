package online.lahloba.www.lahloba.ui.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.ui.address.AddressActivity;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.sub_menu.SubMenuActivity;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

public class AccountAdapter extends BaseAdapter {
    LoginViewModel loginViewModel;
    List<MainMenuItem> accountItemList;
    Context context;

    public AccountAdapter(Context context, LoginViewModel loginViewModel) {
        this.context = context;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public int getCount() {
        return null==accountItemList ? 0 : accountItemList.size();
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
        View view = layoutInflater.inflate(R.layout.row_main_grid, null, false);
        ImageView imageView = view.findViewById(R.id.imageView2);
        TextView titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText(accountItemList.get(position).getTitle());

        Picasso.get()
                .load(accountItemList.get(position).getImage())
                .into(imageView);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountItemList.get(position).getTitle().equals(context.getResources().getString(R.string.my_adresses))){
                    Intent intent = new Intent(context, AddressActivity.class);
                    context.startActivity(intent);
                }else if (accountItemList.get(position).getTitle().equals(context.getResources().getString(R.string.sign_out))){
                    loginViewModel.startLogOut();
                }
            }
        });







        return view;
    }

    public void setAccountItemList(List<MainMenuItem> accountItemList) {
        this.accountItemList = accountItemList;
    }
}
