package online.lahloba.www.lahloba.utils;

import android.content.Context;

import online.lahloba.www.lahloba.AppExecutor;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.VMPHelper;

public class Injector {

    public static AppRepository provideRepository(Context context){
        NetworkDataHelper networkDataHelper = NetworkDataHelper.getInstance(context);
        LahlobaDatabase database = LahlobaDatabase.newInstance(context);

        return AppRepository.getInstance(networkDataHelper, database);
    }

    public static NetworkDataHelper provideNetworkDataHelper(Context context) {
        return NetworkDataHelper.getInstance(context.getApplicationContext());
    }

    public static AppExecutor getExecuter(){
        return AppExecutor.getInstance();
    }


    public static ViewModelProviderFactory getVMFactory(Context context, VMPHelper vmpHelper){
        AppRepository appRepository = Injector.provideRepository(context);
        return new ViewModelProviderFactory(appRepository, vmpHelper);
    }

}
