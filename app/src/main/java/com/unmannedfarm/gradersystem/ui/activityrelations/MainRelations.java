package com.unmannedfarm.gradersystem.ui.activityrelations;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.bean.avtivitybean.BaseStationBean;
import com.unmannedfarm.gradersystem.bean.avtivitybean.MainActivityBean;
import com.unmannedfarm.gradersystem.bean.dataacquisitionbean.STM32DataBean;
import com.unmannedfarm.gradersystem.controller.gpscontroller.ControlDataUtils;
import com.unmannedfarm.gradersystem.controller.gpscontroller.ControllerService;
import com.unmannedfarm.gradersystem.controller.gpscontroller.ControllerServiceConnection;
import com.unmannedfarm.gradersystem.controller.gpscontroller.GraderType;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CAN;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.FrameFormat;
import com.unmannedfarm.gradersystem.dataacquisition.serialport.BaudRate;
import com.unmannedfarm.gradersystem.dataacquisition.serialport.SerialPort;
import com.unmannedfarm.gradersystem.dataacquisition.serialport.SerialPortData;
import com.unmannedfarm.gradersystem.dataacquisition.serialport.SerialPortSource;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessingBean;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessingService;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessingServiceConnection;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataBean;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataProcessing;
import com.unmannedfarm.gradersystem.manager.filemanager.FileManager;
import com.unmannedfarm.gradersystem.manager.filemanager.FileWriter;
import com.unmannedfarm.gradersystem.manager.ntripmanager.GpsDifferentialData;
import com.unmannedfarm.gradersystem.manager.ntripmanager.NetWorkServiceNtrip;
import com.unmannedfarm.gradersystem.manager.preferencemanager.PreferenceManager;
import com.unmannedfarm.gradersystem.ui.activities.BaseStationActivity;
import com.unmannedfarm.gradersystem.ui.activities.DataCollectionActivity;
import com.unmannedfarm.gradersystem.ui.activities.MainActivity;
import com.unmannedfarm.gradersystem.ui.activities.ParamSettingActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.ActivityLifecycleListener;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseExternalRelations;
import com.unmannedfarm.gradersystem.ui.dialog.MyDialog;
import com.unmannedfarm.gradersystem.utils.ActionItem;
import com.unmannedfarm.gradersystem.utils.DataUtils;
import com.unmannedfarm.gradersystem.utils.FilterUtils;
import com.unmannedfarm.gradersystem.utils.LocationUtils;
import com.unmannedfarm.gradersystem.utils.NetworkSignalUtil;
import com.unmannedfarm.gradersystem.utils.TitlePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import tp.xmaihh.serialport.bean.ComBean;

import static java.lang.Thread.sleep;


public class MainRelations extends BaseExternalRelations<MainActivity> {

    private final String TAG = getClass().getSimpleName();
    private final String clientId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? Build.getSerial() : Build.SERIAL;              //获取当前设备唯一序列号
    private DecimalFormat df2 = new DecimalFormat("0.00");
    private DecimalFormat df3 = new DecimalFormat("0.000");
    private ControllerServiceConnection     controllerServiceConnection;
    private DataProcessingServiceConnection dataProcessingServiceConnection;
    private SerialPort                      ttyS0;
    private SerialPort                      ttyS6;
    private CAN                             canBus;
    private TitlePopup titlePopup;
    private FileWriter fileWriter;
    private NetWorkServiceNtrip             netWorkServiceNtrip;
    private final String                    ggaSpeed10Hz = "GPGGA 0.1\r\n";
    private final String                    vtgSpeed10Hz = "GPVTG 0.1\r\n";
    private final String                    traSpeed10Hz = "GPTRA 0.1\r\n";
    private float      totalHeight       = 0;
    private float      count             = 0;
    private float      baseHeight        = 0;
    private float      height            = 0;
    private int        rtkQuality        = 0;
    private boolean    getBaseHeightFlag = false;
    private boolean    startAutoFlag     = false;
    private boolean    isStartAutoFlag   = false;
    private SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss"); //定制时间时间格式，时:分:秒
    private SimpleDateFormat sTimeFormat1 = new SimpleDateFormat("HH-mm-ss"); //定制时间时间格式，年-月-日 时-分-秒

    public MainRelations(MainActivity activity) {
        super(activity);

        fileWriter = new FileWriter();
        fileWriter.createFile(mActivity.getExternalFilesDir(null) + "/bodyData" + sTimeFormat1.format(new Date()) + ".txt", true);

        initOnClick();
        NetworkSignalUtil.getPhoneState(mActivity);
        initControllerService();
        initDataProcessingService();

        initPopupView();
        setInnerPopupView();

    }

    //popup点击时间
    private void setInnerPopupView(){
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (position == 0) {
                    mActivity.startActivity(new Intent(mActivity, ParamSettingActivity.class));
                } else if (position == 1) {
                    mActivity.startActivity(new Intent(mActivity, BaseStationActivity.class));
                } else if (position == 2) {
                    mActivity.startActivity(new Intent(mActivity, DataCollectionActivity.class));
                }
            }
        });
    }

    //初始化popup组件
    private void initPopupView() {
        titlePopup = new TitlePopup(mActivity, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化标题栏按钮并设置监听
        mActivity.setBtnSetting(btnSetting());
        //初始化标题栏中的项目列表
        titlePopup.addAction(new ActionItem(mActivity, "参数设置", R.mipmap.controller));
        titlePopup.addAction(new ActionItem(mActivity, "基站配置", R.mipmap.base_station));
        titlePopup.addAction(new ActionItem(mActivity, "数据采集", R.mipmap.data_collect));
        titlePopup.addAction(new ActionItem(mActivity, "系统日志", R.mipmap.log));
    }

    /**
     * @fun : 开启EventBus
     * */
    @Override
    protected Boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected ActivityLifecycleListener newActivityLifecycleListener() {
        return new ActivityLifecycleListener() {
            @Override
            public void onModulesCreated() {
                super.onModulesCreated();

                initParams();


                /*======================= 开启串口ttyS0接收GPS数据 ========================*/
                MainRelations.this.ttyS0 = new SerialPort(SerialPortSource.com0, BaudRate.rate115200, 0) {
                    @Override
                    protected void onDataReceived(ComBean comBean) {
                        byte[] revData = comBean.bRec;
                        if (revData != null && revData.length > 0) {
                            String dataPart = new String(revData);
                            String data = DataUtils.dataStitching("$GPGGA", dataPart); //数据预处理
                            if (data != null) {
                                SerialPortData serialPortData = new SerialPortData();
                                serialPortData.setRevStrData(data);
                                EventBus.getDefault().post(serialPortData);
                            }
                        }
                    }
                };
                MainRelations.this.ttyS0.open();
                MainRelations.this.ttyS0.sendText(ggaSpeed10Hz);
                MainRelations.this.ttyS0.sendText(vtgSpeed10Hz);
                MainRelations.this.ttyS0.sendText(traSpeed10Hz);

                /*======================= 开启串口ttyS6接收stm32上传数据 ========================*/
                MainRelations.this.ttyS6 = new SerialPort(SerialPortSource.com1, BaudRate.rate115200, 0) {
                    @Override
                    protected void onDataReceived(ComBean comBean) {
                        byte[] revData = comBean.bRec;
                        if (revData != null && revData.length > 0) {
                            String dataPart = new String(revData);
                            String data = DataUtils.dataStitching("$stm32", dataPart);
                            if (data != null) {
                                STM32DataBean stm32DataBean = new STM32DataBean();
                                // 数据预处理
                                stm32DataBean.setADValue(Integer.parseInt(data.substring(data.indexOf(",") + 1)));
                                EventBus.getDefault().post(stm32DataBean);
                            }
                        }
                    }
                };
                
                /*===================== 开启CAN总线 ======================*/
                MainRelations.this.canBus = CAN.getCanInstance(mActivity);
                MainRelations.this.canBus.open();

            }

            @Override
            public void onResume() {
                super.onResume();
                getParamFromStorage();
                connectDiffBaseStation();
            }

            @Override
            public void onDestroy() {
                super.onDestroy();
                fileWriter.close();
                mActivity.unbindService(controllerServiceConnection);
                mActivity.unbindService(dataProcessingServiceConnection);
            }
        };
    }

    private boolean workMode = true;    //默认作业类型是旱平地
    private double lat = 0;
    private double lng = 0;
    private String time;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void revGpsDataBean(GpsDataBean gpsDataBean){
        lat = gpsDataBean.getGpsLat();
        lng = gpsDataBean.getGpsLng();
        rtkQuality = gpsDataBean.getGpsRtkQuality();
        //height = FilterUtils.averageFilter(10, gpsDataBean.getHeightValue()); //滑动滤波
        height = gpsDataBean.getHeightValue();
        time = sTimeFormat.format(new Date());   //获取当前时间

        /*=============================== 参数处理 ================================*/
        if (getBaseHeightFlag) {
            if (count < 30) {
                totalHeight += height;//以主天线作为参考值
            }
            count++;
            if (count == 30) {
                baseHeight = totalHeight / 30;    //参考高程值
                count = 0;
                totalHeight = 0;
                getBaseHeightFlag = false;
                mActivity.setBaseHeightView(String.format("%.2f", baseHeight) + "m");
                mActivity.setBaseHeightNormalView();
            }
        }
        GraderType graderType;
        if (workMode) {
            graderType = GraderType.dryLandGrader;
        } else {
            graderType = GraderType.paddyGrader;
        }

        //System.out.println(gpsDataBean.getTime());   //打印GPGGA的时间戳，检测GPS数据频率是否达到要求
        /*=============================== 界面参数更新 ================================*/
        uiDataUpdate();

        mActivity.setTvNetwork(Integer.parseInt(NetworkSignalUtil.getLteDbm()));
        mActivity.setTvSatelliteNum(gpsDataBean.getSatelliteNumber());
        mActivity.setTvSpeed(gpsDataBean.getSpeed());
        mActivity.setRtkWarning(rtkQuality);
        mActivity.setTvDelay(gpsDataBean.getDiffTimeout());
        mActivity.setTvSN(clientId);
        mActivity.setTvWorkMode(workMode);
        mActivity.setTvTime(time);
        /*=============================== 控制参数更新 ================================*/
        MainActivityBean mainActivityBean = new MainActivityBean();
        mainActivityBean.setStartSaveFlag(startSaveFlag);           //开始保存标志位
        mainActivityBean.setGetBaseHeightFlag(getBaseHeightFlag);   //获取基准高程标志位
        mainActivityBean.setStartAutoFlag(startAutoFlag);           //开启自动平地标志位
        mainActivityBean.setBaseHeight(baseHeight);                 //基准高程值
        mainActivityBean.setAverageHeight(height);                  // 平均高程值
        mainActivityBean.setMachineWidth(machineWidth);             // 机具幅宽
        mainActivityBean.setHeightDeadZoneValue(heightDead);        // 高程死区值
        mainActivityBean.setRollDeadZoneValue(levelDead);           // 调平死区值
        mainActivityBean.setControlSpeedLevel(speedLevel);          // 速度水平等级
        mainActivityBean.setDiffTimeout(gpsDataBean.getDiffTimeout());// 差分延时时间
        mainActivityBean.setGraderType(graderType);                 // 平地类型
        EventBus.getDefault().post(mainActivityBean);

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void baseStationData(BaseStationBean baseStationBean) {
        System.out.println(baseStationBean.toString());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void revDiffData(GpsDifferentialData gpsDifferentialData) {
        ttyS0.send(gpsDifferentialData.getGpsDiffData());
    }

    private float roll = 0;
    private float pitch = 0;
    private float yaw = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void revDataProcessingBean(DataProcessingBean dataProcessingBean) {
        this.roll = dataProcessingBean.getAhrsDataBean01().getPitch();    //根据AHRS安装方式，选用pitch进行控制
        this.pitch = dataProcessingBean.getAhrsDataBean01().getRoll();
        this.yaw = dataProcessingBean.getGpsDataBean().getYaw();

        double lat = dataProcessingBean.getGpsDataBean().getGpsLat();
        double lng = dataProcessingBean.getGpsDataBean().getGpsLng();
        double height = dataProcessingBean.getGpsDataBean().getHeightValue();
        float  yaw = dataProcessingBean.getGpsDataBean().getYaw();

        mActivity.setTvGpsData(lat, lng, Float.parseFloat(df3.format(height)), yaw);
        double[] xyz = LocationUtils.blhToXyz(lat, lng, height);
        mActivity.setTvXY(df3.format(xyz[0]), df3.format(xyz[1]));
        mActivity.setTvAhrsData(df3.format(dataProcessingBean.getAhrsDataBean01().getRoll()), df3.format(dataProcessingBean.getAhrsDataBean01().getPitch()));

    }
    //设置默认参数
    private void initParams() {
        PreferenceManager.saveBooleanValue(mActivity, true, "rbDry");       //默认作业模式为旱平地
        PreferenceManager.saveStringValue(mActivity, "2.2", "machineWidth");//默认机具幅宽为2.2m
        PreferenceManager.saveStringValue(mActivity, "2.0", "heightDead");  //默认高程死区值为2cm
        PreferenceManager.saveStringValue(mActivity, "1.0", "levelDead");   //默认调平死区值为1度
        PreferenceManager.saveStringValue(mActivity, "2", "speedLevel");    //默认控制速度等级为2
    }
    //获取参数设置
    private String ip, port, account, pwd, mtp;
    private float machineWidth, heightDead, levelDead;
    private int speedLevel;
    private void getParamFromStorage() {
        ip      = PreferenceManager.getStringValue(mActivity, "ip");
        port    = PreferenceManager.getStringValue(mActivity, "port");
        account = PreferenceManager.getStringValue(mActivity, "account");
        pwd     = PreferenceManager.getStringValue(mActivity, "pwd");
        mtp     = PreferenceManager.getStringValue(mActivity, "mtp");
        workMode = PreferenceManager.getBooleanValue(mActivity, "rbDry");
        machineWidth = Float.parseFloat(PreferenceManager.getStringValue(mActivity, "machineWidth"));
        heightDead   = Float.parseFloat(PreferenceManager.getStringValue(mActivity, "heightDead"));
        levelDead    = Float.parseFloat(PreferenceManager.getStringValue(mActivity, "levelDead"));
        speedLevel   = Integer.parseInt(PreferenceManager.getStringValue(mActivity, "speedLevel"));
    }
    //连接差分基站
    private void connectDiffBaseStation() {
        if (!ip.isEmpty() && !port.isEmpty() && !account.isEmpty() && !pwd.isEmpty() && !mtp.isEmpty()) {
            netWorkServiceNtrip = new NetWorkServiceNtrip(mActivity, ip, port, account, pwd, mtp);
            netWorkServiceNtrip.getDifferentialData();
        }
    }
    //界面数据参数处理与显示
    private void uiDataUpdate() {
        float baseHeightValue = baseHeight;
        float antennaHeight = height;
        int heightDiff = (int) ((antennaHeight - baseHeightValue) * 100);
        float lrHeightDiff = (float) Math.sin(roll * Math.PI / 180) * machineWidth; //左右高度差

        int rightHeightDiff = (int) (((antennaHeight + lrHeightDiff / 2) - baseHeightValue) * 100);
        int leftHeightDiff  = (int) (((antennaHeight - lrHeightDiff / 2) - baseHeightValue) * 100);

        if (workMode) {
            mActivity.setTvLeftPart(heightDiff,  df2.format(height), heightDead);
            mActivity.setTvRightPart(heightDiff, df2.format(height), heightDead);
        } else {
            mActivity.setTvLeftPart(leftHeightDiff,  df2.format(height  + leftHeightDiff  / 100), heightDead);
            mActivity.setTvRightPart(rightHeightDiff, df2.format(height + rightHeightDiff / 100), heightDead);
        }
    }
    //初始化控制服务
    private void initControllerService() {
        controllerServiceConnection = new ControllerServiceConnection();
        Intent intent = new Intent(mActivity, ControllerService.class);
        mActivity.bindService(intent, controllerServiceConnection, Context.BIND_AUTO_CREATE);
    }
    //初始化数据处理服务
    private void initDataProcessingService() {
        dataProcessingServiceConnection = new DataProcessingServiceConnection();
        Intent intent = new Intent(mActivity, DataProcessingService.class);
        mActivity.bindService(intent, dataProcessingServiceConnection, Context.BIND_AUTO_CREATE);
    }
    //点击显示设置复选框
    private View.OnClickListener btnSetting() {
        return (v) -> {
            titlePopup.show(v);
        };
    }
    //点击状态按键
    private View.OnClickListener btnStatus() {
        return (v) -> {
            mActivity.setFlStatusDataVisibility(true);  //显示状态数据框
        };
    }
    //自动控制

    private View.OnClickListener btnAuto() {
        return (v) -> {
            if (isStartAutoFlag) {
                if (startAutoFlag) {
                    startAutoFlag = false;
                    try {
                        sleep(200);
                        CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(0, 0, 0), FrameFormat.stdFormat);
                        sleep(200);
                        CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(0, 0, 0), FrameFormat.stdFormat);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    startAutoFlag = true;
                }
                mActivity.setTvAutoContent(startAutoFlag);
            } else {
                mActivity.showToast("未获取基准高程");
            }
        };
    }
    //保存设备状态数据
    private int statusDataNum = 1;
    private boolean startSaveFlag = false;
    private View.OnClickListener btnSaveStatusData() {
        return (v) -> {

            startSaveFlag = true;

            String statusData = "<" + statusDataNum + ">" + "," + pitch + "," + roll + "," + yaw + "," + lat + "," + lng + "," + height + "\r\n";
            fileWriter.writeToFile(statusData);
            statusDataNum ++;
        };
    }
    //上升
    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener btnShortUp(){
        return (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mActivity.setLlBtnUpPress();
                CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(1000, 0, 0), FrameFormat.stdFormat);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mActivity.setLlBtnUpNormal();
                CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(0, 0, 0), FrameFormat.stdFormat);
            }
            return true;
        };
    }
    //下降
    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener btnShortDown(){
        return (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mActivity.setLlBtnDownPress();
                CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(-800, 0, 0), FrameFormat.stdFormat);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mActivity.setLlBtnDownNormal();
                CAN.sendCanData(0x37B, ControlDataUtils.CANBridge(0, 0, 0), FrameFormat.stdFormat);
            }
            return true;
        };
    }
    //提高基准高程
    private View.OnClickListener btnPlus() {
        return (v) -> {
            baseHeight += 0.01;
            mActivity.setBaseHeightView(String.format("%.2f", baseHeight) + "m");
        };
    }

    //降低基准高程
    private View.OnClickListener btnMinus() {
        return (v) -> {
            baseHeight -= 0.01;
            mActivity.setBaseHeightView(String.format("%.2f", baseHeight) + "m");
        };
    }

    //获取基准高程值
    private View.OnLongClickListener btnGetBaseHigh() {
        return (v) -> {
            mActivity.setBaseHeightPressView();
            getBaseHeightFlag = true;
            isStartAutoFlag = true;
            return false;
        };
    }
    //初始化按键点击事件
    private void initOnClick() {
        mActivity.setBtnStatus(btnStatus());
        mActivity.setBtnAuto(btnAuto());
        mActivity.setBtnSaveStatusData(btnSaveStatusData());
        mActivity.setBtnShortUp(btnShortUp());
        mActivity.setBtnShortDown(btnShortDown());
        mActivity.setBtnPlus(btnPlus());
        mActivity.setBtnMinus(btnMinus());
        mActivity.setBtnBaseHigh(btnGetBaseHigh());
    }

    public void dialogLookStatus(){
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //默认不显示软键盘
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.dialog_status, null);

        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setBackgroundDrawableResource(android.R.color.);// 一句话搞定
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        lp.y = 150; // 新位置Y坐标
        lp.width = (int) mActivity.getResources().getDisplayMetrics().widthPixels; // 宽度
        //lp.width = 650;
        lp.alpha = 0.7f;
        dialogWindow.setAttributes(lp);
        dialog.show();

        TextView tvLat = view.findViewById(R.id.dialog_lat);
        TextView tvLng = view.findViewById(R.id.dialog_lng);
        TextView tvHeight = view.findViewById(R.id.dialog_height);
        TextView tvRoll = view.findViewById(R.id.dialog_roll);


    }



}
