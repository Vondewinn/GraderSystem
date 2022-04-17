package com.unmannedfarm.gradersystem.controller.gpscontroller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.unmannedfarm.gradersystem.bean.avtivitybean.MainActivityBean;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CAN;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.FrameFormat;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessingBean;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataBean;
import com.unmannedfarm.gradersystem.ui.activities.MainActivity;
import com.unmannedfarm.gradersystem.utils.FilterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @fun    : controller,including data processing and controller
 * @author : Vondewinn
 * @date   : 2022-03-08
 * @param  ：入口参数（实时高程值、基准高程值、横滚角和平地机类型）与出口变量值（三路pwm）
 * */

public class ControllerService extends Service {

    private final String TAG = getClass().getSimpleName();
    private GraderType graderType = GraderType.dryLandGrader;
    private float height = 0;
    private float baseHeightValue = 0;
    private float heightDeadZoneValue = 0;
    private float rollDeadZoneValue = 0;
    private int   speedLevel = 0;
    private int   heightDiff = 0;
    private float roll = 0;
    private int controllerPwm = 0;
    private boolean startAutoFlag = false;
    private boolean getBaseHeightFlag = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "Controller onBind");
        return new CustomBinder();
    }

    public class CustomBinder extends Binder {
        public ControllerService getService() {
            return ControllerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        new startActuator().start();
    }

    //获取到MainActivity发过的更新数据
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revMainActivity(MainActivityBean mainActivityBean) {
        graderType          = mainActivityBean.getGraderType();
        heightDeadZoneValue = mainActivityBean.getHeightDeadZoneValue(); //获取高程死区值
        rollDeadZoneValue   = mainActivityBean.getRollDeadZoneValue();   //获取角度死区值
        speedLevel          = mainActivityBean.getControlSpeedLevel();   //获取控制速度等级
        startAutoFlag       = mainActivityBean.isStartAutoFlag();        //获取自动平地标志位
        height              = mainActivityBean.getAverageHeight();       //获取高程值
        baseHeightValue     = mainActivityBean.getBaseHeight();
    }

    /**
     * 问题：
     * 1、想办法获取到基准高程值
     * 2、想办法获取到死区值，需要用现在的PreferenceManager去替换原来的Preference
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void controller(DataProcessingBean dataProcessingBean) {

        float rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
        float diffTimeout = dataProcessingBean.getGpsDataBean().getDiffTimeout();

        byte[] data;
        int pwm1 = 0, pwm2 = 0, pwm3 = 0;

        if (speedLevel <= 1) controllerPwm = -600;
        else if (speedLevel == 2) controllerPwm = -650;
        else if (speedLevel == 3) controllerPwm = -700;
        else if (speedLevel == 4) controllerPwm = -800;
        else controllerPwm = -900;

        switch (graderType) {
            case paddyGrader:
                heightDiff = (int) ((height - baseHeightValue) * 100);
                roll = dataProcessingBean.getAhrsDataBean01().getPitch();
                if (startAutoFlag) {
                    if (rtkQuality == 4) {
                        if (diffTimeout <= 100) {
                            if (heightDiff > heightDeadZoneValue) {
                                //todo 平地铲往下降
                                pwm1 = controllerPwm;
                            } else if (heightDiff < -heightDeadZoneValue/2) {
                                //todo 平地铲往上升
                                pwm1 = 1000;
                            } else {
                                //todo pwm输出为0
                                pwm1 = 0;
                            }

                            if (roll > rollDeadZoneValue) {
                                pwm2 = 1000;
                            } else if (roll < -rollDeadZoneValue) {
                                pwm2 = controllerPwm;
                            } else {
                                //todo 不需要调整
                                pwm2 = 0;
                            }

                            data = ControlDataUtils.CANBridge(pwm1, pwm2, pwm3);
                            CAN.sendCanData(0x37B, data, FrameFormat.stdFormat);
                        } else {
                            Toast.makeText(this, "差分延时过大", Toast.LENGTH_SHORT);
                        }
                    } else {
                        Toast.makeText(this, "差分信号丢失", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case dryLandGrader:
                heightDiff = (int) ((height - baseHeightValue) * 100);
                if (startAutoFlag) {
                    if (rtkQuality == 4) {
                        if (diffTimeout <= 100) {
                            if (heightDiff > heightDeadZoneValue) {
                                //todo 平地铲往下降
                                pwm1 = controllerPwm;
                            } else if (heightDiff < -heightDeadZoneValue/2) {
                                //todo 平地铲往上升
                                pwm1 = 1000;
                            } else {
                                //todo pwm输出为0
                                pwm1 = 0;
                            }

                            data = ControlDataUtils.CANBridge(pwm1, pwm2, pwm3);
                            CAN.sendCanData(0x37B, data, FrameFormat.stdFormat);
                        } else {
                            Toast.makeText(this, "差分延时过大", Toast.LENGTH_SHORT);
                        }
                    } else {
                        Toast.makeText(this, "差分信号丢失", Toast.LENGTH_SHORT);
                    }
                }
                break;
        }

    }

    private byte[] openMD2IO        = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; // 执行器MD2开启
    private byte[] MD2HeartbeatPack = new byte[]{0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}; // 执行器MD2心跳包

    private class startActuator extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(500);  //缓冲500ms，必须
                CAN.sendCanData(0x000, openMD2IO, FrameFormat.stdFormat);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!interrupted()){
                try {
                    CAN.sendCanData(0x77F, MD2HeartbeatPack, FrameFormat.stdFormat);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
