package online.lahloba.www.lahloba.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.services.LahlobaMainService;
import online.lahloba.www.lahloba.utils.LocalUtils;

import static online.lahloba.www.lahloba.utils.Constants.GET_MAIN_MENU_ITEMS;

public class NetworkDataHelper {
    private static final Object LOCK = new Object();
    private static NetworkDataHelper sInstance;
    Context mContext;

    MutableLiveData<List<MainMenuItem>> mainMenuItems;


    public NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
    }

    public static NetworkDataHelper getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new NetworkDataHelper(context.getApplicationContext());
            }
        }

        return sInstance;
    }



    //############################### Main Menu Item ############################//

    public void startGetMainMenuItems() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_MAIN_MENU_ITEMS);
        mContext.startService(intent);

    }

    public void startFetchMainMenuItems() {

        String language = LocalUtils.getLangauge();
        DatabaseReference database = FirebaseDatabase
                .getInstance()
                .getReference("MainMenu")
                .child(language);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MainMenuItem> mainMenuItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    MainMenuItem item = child.getValue(MainMenuItem.class);
                    mainMenuItemList.add(item);
                }

                mainMenuItems.setValue(mainMenuItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public MutableLiveData<List<MainMenuItem>> getMainMenuItems() {
        return mainMenuItems;
    }
}
