package online.lahloba.www.lahloba.ui.cart.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.BottomSheetAdressBinding;
import online.lahloba.www.lahloba.ui.adapters.CartAddressAdapter;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;

public class AddressBottomSheet extends BottomSheetDialogFragment
        implements CartAddressAdapter.OnAddressSelected {

    private List<AddressItem> addresses;
    CartAddressAdapter cartAddressAdapter;
    RecyclerView addressRv;
    SendAddressToCart sendAddressToCart;
    CartViewModel cartViewModel;

    public AddressBottomSheet() {
        addresses = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetAdressBinding binding = BottomSheetAdressBinding.inflate(inflater,container,false);
        addressRv = binding.addressRV;
        cartAddressAdapter = new CartAddressAdapter(this);
        cartAddressAdapter.setAddressItems(addresses);

        addressRv.setLayoutManager(new LinearLayoutManager(getContext()));
        addressRv.setAdapter(cartAddressAdapter);

        cartViewModel.startGetAddress(FirebaseAuth.getInstance().getUid());



        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();


        cartViewModel.getAddresses().observe(getActivity(), addresses->{
            if (addresses==null)return;

            setAddresses(addresses);

        });

    }

    public void setAddresses(List<AddressItem> addresses) {
        this.addresses.clear();
        this.addresses.addAll(addresses);

        if (cartAddressAdapter != null)
        cartAddressAdapter.notifyDataSetChanged();
    }


    /**
     * Select Address
     */

    @Override
    public void onAddressItemSelected(AddressItem addressItems) {
        sendAddressToCart.onSendAddressToCart(addressItems);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            sendAddressToCart = (SendAddressToCart) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement SendAddressToCartFragment");
        }

    }


    public interface SendAddressToCart{
        void onSendAddressToCart(AddressItem addressItems);
    }


    public void setCartViewModel(CartViewModel cartViewModel) {
        this.cartViewModel = cartViewModel;
    }
}
