package com.unmannedfarm.gradersystem.dataprocessing;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.unmannedfarm.gradersystem.bean.avtivitybean.DataCollectionBean;
import com.unmannedfarm.gradersystem.bean.avtivitybean.MainActivityBean;
import com.unmannedfarm.gradersystem.bean.dataacquisitionbean.STM32DataBean;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CanData;
import com.unmannedfarm.gradersystem.dataacquisition.serialport.SerialPortData;
import com.unmannedfarm.gradersystem.dataprocessing.ahrsdata.AhrsDataBean;
import com.unmannedfarm.gradersystem.dataprocessing.ahrsdata.AhrsDataProcessing;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataBean;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataProcessing;
import com.unmannedfarm.gradersystem.manager.filemanager.FileManager;
import com.unmannedfarm.gradersystem.manager.filemanager.FileWriter;
import com.unmannedfarm.gradersystem.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @fun    : data processing
 * @author : Vondewinn
 * @date   : 2022-03-17
 * @param  :
 * @notes  : AHRS_50Hz, GPS_10Hz, 拉线传感器_50Hz
 * */

public class DataProcessingService extends Service {

    private final String TAG = getClass().getSimpleName();
    private GpsDataProcessing  gpsDataProcessing  = new GpsDataProcessing();
    private AhrsDataProcessing ahrs01;
    private AhrsDataProcessing ahrs02;
    private DataProcessingBean dataProcessingBean = new DataProcessingBean();
    private GpsDataBean gpsDataBean = new GpsDataBean();
    private AhrsDataBean ahrsDataBean01 = new AhrsDataBean();
    private AhrsDataBean ahrsDataBean02 = new AhrsDataBean();
    private FileManager fileManager;
    private SimpleDateFormat sTimeFormat;
    private SimpleDateFormat saveTimeFormat;
    private String date;
    private boolean startSaveFlag = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "DataProcessing onBind");
        return new CustomBinder();
    }

    public class CustomBinder extends Binder {
        public DataProcessingService getService() {
            return DataProcessingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        ahrs01 = new AhrsDataProcessing(0x050);
        ahrs02 = new AhrsDataProcessing(0x051);
        sTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        date = sTimeFormat.format(new Date());
        saveTimeFormat = new SimpleDateFormat("HH-mm-ss");

        fileManager = new FileManager(this.getExternalFilesDir(null) + "/data" + date + ".txt");
        fileManager.open();

        new sendDataThread().start();

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revMainActivity(MainActivityBean mainActivityBean) {
        //startSaveFlag = mainActivityBean.isStartSaveFlag();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revDataCollectionActivity(DataCollectionBean dataCollectionBean) {
        startSaveFlag = dataCollectionBean.isStartSaveFlag();
    }

    private int num = 0;
    private int status01TestNum = 0;
    private int status02TestNum = 0;
    private boolean ahrs01Flag = false;
    private boolean ahrs02Flag = false;
    private short status01 = 0;
    private short status02 = 0;
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revCanData(CanData canData) {

        ahrsDataBean02 = (AhrsDataBean) ahrs02.dataAnalysis(canData);
        ahrsDataBean01 = (AhrsDataBean) ahrs01.dataAnalysis(canData);

//        if (ahrsDataBean01 != null){
//            dataProcessingBean.setAhrsDataBean01(ahrsDataBean01);
//            ahrs01Flag = true;
//        }
//
//        if (ahrsDataBean02 != null){
//            dataProcessingBean.setAhrsDataBean02(ahrsDataBean02);
//            ahrs02Flag = true;
//        }

//        if (ahrs01Flag && ahrs02Flag) {
//            String time = saveTimeFormat.format(new Date());
//            int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
//            double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
//            double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
//            float height = dataProcessingBean.getGpsDataBean().getHeightValue();
//            float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
//            double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
//            double x = xyz[0];
//            double y = xyz[1];
//            float roll01 = dataProcessingBean.getAhrsDataBean01().getRoll();
//            float pitch01 = dataProcessingBean.getAhrsDataBean01().getPitch();
//            float yaw01 = dataProcessingBean.getAhrsDataBean01().getYaw();
//            float gyroX01 = dataProcessingBean.getAhrsDataBean01().getGyroX();
//            float gyroY01 = dataProcessingBean.getAhrsDataBean01().getGyroY();
//            float gyroZ01 = dataProcessingBean.getAhrsDataBean01().getGyroZ();
//            float ax01 = dataProcessingBean.getAhrsDataBean01().getAx();
//            float ay01 = dataProcessingBean.getAhrsDataBean01().getAy();
//            float az01 = dataProcessingBean.getAhrsDataBean01().getAz();
//            float roll02 = dataProcessingBean.getAhrsDataBean02().getRoll();
//            float pitch02 = dataProcessingBean.getAhrsDataBean02().getPitch();
//            float yaw02 = dataProcessingBean.getAhrsDataBean02().getYaw();
//            float gyroX02 = dataProcessingBean.getAhrsDataBean02().getGyroX();
//            float gyroY02 = dataProcessingBean.getAhrsDataBean02().getGyroY();
//            float gyroZ02 = dataProcessingBean.getAhrsDataBean02().getGyroZ();
//            float ax02 = dataProcessingBean.getAhrsDataBean02().getAx();
//            float ay02 = dataProcessingBean.getAhrsDataBean02().getAy();
//            float az02 = dataProcessingBean.getAhrsDataBean02().getAz();
//            // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
//            // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS01横滚角，AHRS01俯仰角，AHRS01航向角，AHRS01x轴角速度，AHRS01y轴角速度，AHRS01z轴角速度，AHRS01x轴加速度，AHRS01y轴加速度，AHRS01z轴加速度，AHRS02横滚角，AHRS02俯仰角，AHRS02航向角，AHRS02x轴角速度，AHRS02y轴角速度，AHRS02z轴角速度，AHRS02x轴加速度，AHRS02y轴加速度，AHRS02z轴加速度"
//            String data = "<" + num + ">" + "," + time + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
//                    + x + "," + y + "," + gpsYaw + ","
//                    + roll01 + "," + pitch01 + "," + yaw01 + ","
//                    + gyroX01 + "," + gyroY01 + "," + gyroZ01 + ","
//                    + ax01 + "," + ay01 + "," + az01 + ","
//                    + roll02 + "," + pitch02 + "," + yaw02 + ","
//                    + gyroX02 + "," + gyroY02 + "," + gyroZ02 + ","
//                    + ax02 + "," + ay02 + "," + az02
//                    + "\r\n";
//            if (startSaveFlag) {
//                fileManager.write(data);
//                num++;
//            }
//            ahrs01Flag = false;
//            ahrs02Flag = false;
//        }

        if (ahrsDataBean01 != null){
            dataProcessingBean.setAhrsDataBean01(ahrsDataBean01);
            ahrs01Flag = true;
            status01TestNum = 0;
            status01 = 1;               // 设备在线状态保存值，status置1表示设备01在线
        } else {
            status01TestNum ++;
            if (status01TestNum > 12) { // 设备在线检测，当设备不在线时，返回对象ahrsDataBean01第3次为空时，则认为设备当前不在线
                status01 = 0;           // 设备不在线，则保存状态置0
                ahrs01Flag = true;
            }
            if (status01TestNum == Integer.MAX_VALUE) status01TestNum = 0;
        }

        if (ahrsDataBean02 != null){
            dataProcessingBean.setAhrsDataBean02(ahrsDataBean02);
            ahrs02Flag = true;
            status02TestNum = 0;
            status02 = 2;               // 同上
        } else {
            status02TestNum ++;
            if (status02TestNum > 12) { // 同上
                status02 = 0;           // 同上
                ahrs02Flag = true;
            }
            if (status02TestNum == Integer.MAX_VALUE) status02TestNum = 0;
        }

        if (ahrs01Flag && ahrs02Flag) {
            short status = (short) (status01 + status02);

            if (status == 0) {
                int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
                double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
                double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
                float height = dataProcessingBean.getGpsDataBean().getHeightValue();
                float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
                double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
                double x = xyz[0];
                double y = xyz[1];
                float roll01 = 0;
                float pitch01 = 0;
                float yaw01 = 0;
                float gyroX01 = 0;
                float gyroY01 = 0;
                float gyroZ01 = 0;
                float ax01 = 0;
                float ay01 = 0;
                float az01 = 0;
                float roll02 = 0;
                float pitch02 = 0;
                float yaw02 = 0;
                float gyroX02 = 0;
                float gyroY02 = 0;
                float gyroZ02 = 0;
                float ax02 = 0;
                float ay02 = 0;
                float az02 = 0;
                // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
                // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS横滚角，AHRS俯仰角，AHRS航向角，x轴加速度，y轴加速度，z轴加速度"
                String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
                        + x + "," + y + "," + gpsYaw + ","
                        + roll01 + "," + pitch01 + "," + yaw01 + ","
                        + gyroX01 + "," + gyroY01 + "," + gyroZ01 + ","
                        + ax01 + "," + ay01 + "," + az01 + ","
                        + roll02 + "," + pitch02 + "," + yaw02 + ","
                        + gyroX02 + "," + gyroY02 + "," + gyroZ02 + ","
                        + ax02 + "," + ay02 + "," + az02
                        + "\r\n";
                System.out.println(data);
                if (startSaveFlag && dataProcessingBean.getGpsDataBean().getGpsRtkQuality() == 4) {
                    fileManager.write(data);
                    num++;
                }
            } else if (status == 1) {
                int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
                double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
                double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
                float height = dataProcessingBean.getGpsDataBean().getHeightValue();
                float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
                double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
                double x = xyz[0];
                double y = xyz[1];
                float roll01 = dataProcessingBean.getAhrsDataBean01().getRoll();
                float pitch01 = dataProcessingBean.getAhrsDataBean01().getPitch();
                float yaw01 = dataProcessingBean.getAhrsDataBean01().getYaw();
                float gyroX01 = dataProcessingBean.getAhrsDataBean01().getGyroX();
                float gyroY01 = dataProcessingBean.getAhrsDataBean01().getGyroY();
                float gyroZ01 = dataProcessingBean.getAhrsDataBean01().getGyroZ();
                float ax01 = dataProcessingBean.getAhrsDataBean01().getAx();
                float ay01 = dataProcessingBean.getAhrsDataBean01().getAy();
                float az01 = dataProcessingBean.getAhrsDataBean01().getAz();
                float roll02 = 0;
                float pitch02 = 0;
                float yaw02 = 0;
                float gyroX02 = 0;
                float gyroY02 = 0;
                float gyroZ02 = 0;
                float ax02 = 0;
                float ay02 = 0;
                float az02 = 0;
                // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
                // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS横滚角，AHRS俯仰角，AHRS航向角，x轴加速度，y轴加速度，z轴加速度"
                String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
                        + x + "," + y + "," + gpsYaw + ","
                        + roll01 + "," + pitch01 + "," + yaw01 + ","
                        + gyroX01 + "," + gyroY01 + "," + gyroZ01 + ","
                        + ax01 + "," + ay01 + "," + az01 + ","
                        + roll02 + "," + pitch02 + "," + yaw02 + ","
                        + gyroX02 + "," + gyroY02 + "," + gyroZ02 + ","
                        + ax02 + "," + ay02 + "," + az02
                        + "\r\n";
                System.out.println(data);
                if (startSaveFlag && dataProcessingBean.getGpsDataBean().getGpsRtkQuality() == 4) {
                    fileManager.write(data);
                    num++;
                }
            } else if (status == 2) {
                int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
                double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
                double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
                float height = dataProcessingBean.getGpsDataBean().getHeightValue();
                float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
                double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
                double x = xyz[0];
                double y = xyz[1];
                float roll01 = 0;
                float pitch01 = 0;
                float yaw01 = 0;
                float gyroX01 = 0;
                float gyroY01 = 0;
                float gyroZ01 = 0;
                float ax01 = 0;
                float ay01 = 0;
                float az01 = 0;
                float roll02 = dataProcessingBean.getAhrsDataBean02().getRoll();
                float pitch02 = dataProcessingBean.getAhrsDataBean02().getPitch();
                float yaw02 = dataProcessingBean.getAhrsDataBean02().getYaw();
                float gyroX02 = dataProcessingBean.getAhrsDataBean02().getGyroX();
                float gyroY02 = dataProcessingBean.getAhrsDataBean02().getGyroY();
                float gyroZ02 = dataProcessingBean.getAhrsDataBean02().getGyroZ();
                float ax02 = dataProcessingBean.getAhrsDataBean02().getAx();
                float ay02 = dataProcessingBean.getAhrsDataBean02().getAy();
                float az02 = dataProcessingBean.getAhrsDataBean02().getAz();
                // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
                // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS横滚角，AHRS俯仰角，AHRS航向角，x轴加速度，y轴加速度，z轴加速度"
                String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
                        + x + "," + y + "," + gpsYaw + ","
                        + roll01 + "," + pitch01 + "," + yaw01 + ","
                        + gyroX01 + "," + gyroY01 + "," + gyroZ01 + ","
                        + ax01 + "," + ay01 + "," + az01 + ","
                        + roll02 + "," + pitch02 + "," + yaw02 + ","
                        + gyroX02 + "," + gyroY02 + "," + gyroZ02 + ","
                        + ax02 + "," + ay02 + "," + az02
                        + "\r\n";
                System.out.println(data);
                if (startSaveFlag && dataProcessingBean.getGpsDataBean().getGpsRtkQuality() == 4) {
                    fileManager.write(data);
                    num++;
                }
            } else if (status == 3) {
                int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
                double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
                double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
                float height = dataProcessingBean.getGpsDataBean().getHeightValue();
                float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
                double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
                double x = xyz[0];
                double y = xyz[1];
                float roll01 = dataProcessingBean.getAhrsDataBean01().getRoll();
                float pitch01 = dataProcessingBean.getAhrsDataBean01().getPitch();
                float yaw01 = dataProcessingBean.getAhrsDataBean01().getYaw();
                float gyroX01 = dataProcessingBean.getAhrsDataBean01().getGyroX();
                float gyroY01 = dataProcessingBean.getAhrsDataBean01().getGyroY();
                float gyroZ01 = dataProcessingBean.getAhrsDataBean01().getGyroZ();
                float ax01 = dataProcessingBean.getAhrsDataBean01().getAx();
                float ay01 = dataProcessingBean.getAhrsDataBean01().getAy();
                float az01 = dataProcessingBean.getAhrsDataBean01().getAz();
                float roll02 = dataProcessingBean.getAhrsDataBean02().getRoll();
                float pitch02 = dataProcessingBean.getAhrsDataBean02().getPitch();
                float yaw02 = dataProcessingBean.getAhrsDataBean02().getYaw();
                float gyroX02 = dataProcessingBean.getAhrsDataBean02().getGyroX();
                float gyroY02 = dataProcessingBean.getAhrsDataBean02().getGyroY();
                float gyroZ02 = dataProcessingBean.getAhrsDataBean02().getGyroZ();
                float ax02 = dataProcessingBean.getAhrsDataBean02().getAx();
                float ay02 = dataProcessingBean.getAhrsDataBean02().getAy();
                float az02 = dataProcessingBean.getAhrsDataBean02().getAz();
                // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
                // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS横滚角，AHRS俯仰角，AHRS航向角，x轴加速度，y轴加速度，z轴加速度"
                String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
                        + x + "," + y + "," + gpsYaw + ","
                        + roll01 + "," + pitch01 + "," + yaw01 + ","
                        + gyroX01 + "," + gyroY01 + "," + gyroZ01 + ","
                        + ax01 + "," + ay01 + "," + az01 + ","
                        + roll02 + "," + pitch02 + "," + yaw02 + ","
                        + gyroX02 + "," + gyroY02 + "," + gyroZ02 + ","
                        + ax02 + "," + ay02 + "," + az02
                        + "\r\n";
                System.out.println(data);
                if (startSaveFlag && dataProcessingBean.getGpsDataBean().getGpsRtkQuality() == 4) {
                    fileManager.write(data);
                    num++;
                }
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revStm32Data(STM32DataBean stm32DataBean) {

        int ADValue = stm32DataBean.getADValue();
        //todo 需要输入拟合关系式得到拉线长度

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void revGpsData(SerialPortData serialPortData) {

        gpsDataBean = (GpsDataBean) gpsDataProcessing.dataAnalysis(serialPortData.getRevStrData());
        dataProcessingBean.setGpsDataBean(gpsDataBean);
        EventBus.getDefault().post(gpsDataBean);

//        if (startSaveFlag && gpsDataBean.getGpsRtkQuality() == 4) {
//            int rtkQuality = dataProcessingBean.getGpsDataBean().getGpsRtkQuality();
//            double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
//            double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
//            float height = dataProcessingBean.getGpsDataBean().getHeightValue();
//            float gpsYaw = dataProcessingBean.getGpsDataBean().getYaw();
//            double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
//            double x = xyz[0];
//            double y = xyz[1];
//            float ahrsRoll = dataProcessingBean.getAhrsDataBean01().getRoll();
//            float ahrsPitch = dataProcessingBean.getAhrsDataBean01().getPitch();
//            float ahrsYaw = dataProcessingBean.getAhrsDataBean01().getYaw();
//            float ax = dataProcessingBean.getAhrsDataBean01().getAx();
//            float ay = dataProcessingBean.getAhrsDataBean01().getAy();
//            float az = dataProcessingBean.getAhrsDataBean01().getAz();
//            // 格式: "<num>,rtkQuality,lat,lng,height,x,y,gpsYaw,ahrsRoll,ahrsPitch,ahrsYaw,ax,ay,az\r\n"
//            // 格式: "<序号>，RTK质量因子，纬度，经度，高程，东，北，GPS航向角，AHRS横滚角，AHRS俯仰角，AHRS航向角，x轴加速度，y轴加速度，z轴加速度"
////            String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
////                    + x + "," + y + "," + gpsYaw + "," + ahrsRoll + "," + ahrsPitch + "," + ahrsYaw
////                    + "," + ax + "," + ay + "," + az + "\r\n";
////            fileManager.write(data);
//            float ahrsRoll1 = dataProcessingBean.getAhrsDataBean01().getRoll1();
//            float ahrsPitch1 = dataProcessingBean.getAhrsDataBean01().getPitch1();
//            float ahrsYaw1 = dataProcessingBean.getAhrsDataBean01().getYaw1();
//            float ahrsRoll2 = dataProcessingBean.getAhrsDataBean01().getRoll2();
//            float ahrsPitch2 = dataProcessingBean.getAhrsDataBean01().getPitch2();
//            float ahrsYaw2 = dataProcessingBean.getAhrsDataBean01().getYaw2();
//            float ahrsRoll3 = dataProcessingBean.getAhrsDataBean01().getRoll3();
//            float ahrsPitch3 = dataProcessingBean.getAhrsDataBean01().getPitch3();
//            float ahrsYaw3 = dataProcessingBean.getAhrsDataBean01().getYaw3();
//            String data = "<" + num + ">" + "," + rtkQuality + "," + lat + "," + lng + "," + height + ","
//                    + x + "," + y + "," + gpsYaw
//                    + "," + ahrsRoll + "," + ahrsPitch + "," + ahrsYaw
//                    + "," + ahrsRoll1 + "," + ahrsPitch1 + "," + ahrsYaw1
//                    + "," + ahrsRoll2 + "," + ahrsPitch2 + "," + ahrsYaw2
//                    + "," + ahrsRoll3 + "," + ahrsPitch3 + "," + ahrsYaw3
//                    + "\r\n";
//            fileWriter.writeToFile(data);
//            num++;
//        }

    }

    /**
     * @fun:  : 界面数据刷新线程
     * @param :
     * */
    class UIUpdateThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted()) {
                try {
                    Thread.sleep(100); //修改界面数据刷新频率，当前刷新频率是10Hz
                    // todo 目前没有做
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @fun: 开启一个子线程，可改变控制时间发送，即控制频率
     * @param : 控制频率为20Hz
    */
    class sendDataThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted()) {
                try {
                    Thread.sleep(50); //修改控制时间
                    EventBus.getDefault().post(dataProcessingBean);
//                    System.out.println(dataProcessingBean.getAhrsDataBean().toString());
//                    System.out.println(dataProcessingBean.getGpsDataBean().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fileManager.close();
    }

}
