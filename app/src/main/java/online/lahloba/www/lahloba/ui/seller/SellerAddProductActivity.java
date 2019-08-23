package online.lahloba.www.lahloba.ui.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.util.concurrent.Executor;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;


public class SellerAddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_add_product_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SellerAddProductFragment.newInstance())
                    .commitNow();

            Intent intent = getIntent();


            Injector.getExecuter().diskIO().execute(()->{

            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (intent.hasExtra(EXTRA_SUBTITLE_ID)){
                        ((SellerAddProductFragment)getSupportFragmentManager().getFragments().get(0))
                                .setCategory(intent.getStringExtra(EXTRA_SUBTITLE_ID));
                    }
                }
            }, 1000);


        }
    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            // Get a list of picked images
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);

            ((SellerAddProductFragment)getSupportFragmentManager().getFragments().get(0))
                    .pickImage(image);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
