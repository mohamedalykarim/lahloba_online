package online.lahloba.www.lahloba.ui.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import online.lahloba.www.lahloba.R;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;
import static online.lahloba.www.lahloba.utils.Constants.PRODUCT_ID;


public class SellerEditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_edit_product_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerEditProductFragment.newInstance())
                    .commitNow();


            Intent intent = getIntent();

            if (intent.hasExtra(PRODUCT_ID)){
                ((SellerEditProductFragment)getSupportFragmentManager().getFragments().get(0))
                        .setProductId(intent.getStringExtra(PRODUCT_ID));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ((SellerEditProductFragment)getSupportFragmentManager().getFragments().get(0))
                .reset();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            // Get a list of picked images
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            ((SellerEditProductFragment)getSupportFragmentManager().getFragments().get(0))
                    .pickImage(image);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
