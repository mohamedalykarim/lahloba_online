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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.services.LahlobaMainService;
import online.lahloba.www.lahloba.utils.LocalUtils;

import static online.lahloba.www.lahloba.utils.Constants.GET_MAIN_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;

public class NetworkDataHelper {
    private static final Object LOCK = new Object();
    private static NetworkDataHelper sInstance;
    Context mContext;

    MutableLiveData<List<MainMenuItem>> mainMenuItems;
    MutableLiveData<List<SubMenuItem>> subMenuItems;


    public NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
        subMenuItems = new MutableLiveData<>();
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


    //############################### Main Menu Item ############################//
    public void startGetSupMenuItems(String subMenuId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_SUB_MENU_ITEMS);
        intent.putExtra(GET_SUB_MENU_ITEMS, subMenuId);
        mContext.startService(intent);
    }

    public void startFetchSubMenuItems(String subMenuId) {
        String language = LocalUtils.getLangauge();
        Query database = FirebaseDatabase
                .getInstance()
                .getReference("SubMenu")
                .child(language).orderByChild("parentId").equalTo(subMenuId);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SubMenuItem> subMenuItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    SubMenuItem item = child.getValue(SubMenuItem.class);
                    subMenuItemList.add(item);
                }
                subMenuItems.setValue(subMenuItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return subMenuItems;
    }
}
