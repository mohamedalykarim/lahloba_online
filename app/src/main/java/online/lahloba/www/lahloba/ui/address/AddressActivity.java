package online.lahloba.www.lahloba.ui.address;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.ui.address.bottom_sheet.EditAddressBottomSheet;
import online.lahloba.www.lahloba.ui.interfaces.EditAddressFromFragmentListener;

public class AddressActivity extends AppCompatActivity implements EditAddressFromFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddressFragment.newInstance())
                    .commitNow();

            ((AddressFragment) getSupportFragmentManager().getFragments().get(0)).setEditAddressFromFragmentListener(this);
        }
    }



    @Override
    public void onClickEditAddressFromFragment(AddressItem addressItem) {
        EditAddressBottomSheet editAddressBottomSheet = new EditAddressBottomSheet();
        editAddressBottomSheet.show(getSupportFragmentManager(), "");
        editAddressBottomSheet.setAddressViewModel(((AddressFragment) getSupportFragmentManager().getFragments().get(0)).mViewModel);
        editAddressBottomSheet.setAddressItem(addressItem);

    }
}
