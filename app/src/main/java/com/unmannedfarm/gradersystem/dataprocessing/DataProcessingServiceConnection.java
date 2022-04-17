package com.unmannedfarm.gradersystem.dataprocessing;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DataProcessingServiceConnection implements ServiceConnection {

    private DataProcessingService dataProcessingService;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        dataProcessingService = ((DataProcessingService.CustomBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public DataProcessingService getDataProcessingService() {
        return dataProcessingService;
    }


}
