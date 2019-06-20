package online.lahloba.www.lahloba.ui.sub_menu;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.repository.SubMenuRepository;

public class SubMenuViewModel extends ViewModel {
    SubMenuRepository appRepository;

    public SubMenuViewModel(SubMenuRepository appRepository) {
        this.appRepository = appRepository;


    }

    public void startSupMenuItems(String subMenuID){
        appRepository.clearSupMenu();
        appRepository.startGetSubMenuItems(subMenuID);
    }

    public MutableLiveData<List<SubMenuItem>> getSupMenuItems(){
        return appRepository.getSubMenuItems();
    }


}
