package online.lahloba.www.lahloba.ui.sub_menu;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;

public class SubMenuViewModel extends ViewModel {
    AppRepository appRepository;

    public SubMenuViewModel(AppRepository appRepository, VMPHelper vmpHelper) {
        this.appRepository = appRepository;
        appRepository.startGetSubMenuItems(vmpHelper.getSubMenuId());
    }

    public MutableLiveData<List<SubMenuItem>> getSupMenuItems(){
        return appRepository.getSubMenuItems();
    }


}
