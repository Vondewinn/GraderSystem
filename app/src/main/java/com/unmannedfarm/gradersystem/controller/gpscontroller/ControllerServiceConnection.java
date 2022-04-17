package com.unmannedfarm.gradersystem.controller.gpscontroller;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ControllerServiceConnection implements ServiceConnection {

    private ControllerService controllerService;


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        controllerService = ((ControllerService.CustomBinder) service).getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public ControllerService getControllerService() {
        return controllerService;
    }

}
