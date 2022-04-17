package com.unmannedfarm.gradersystem.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.manager.preferencemanager.PreferenceManager;
import com.unmannedfarm.gradersystem.ui.activityrelations.MainRelations;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseActivity;
import com.unmannedfarm.gradersystem.utils.ActionItem;
import com.unmannedfarm.gradersystem.utils.AnimationVisible;
import com.unmannedfarm.gradersystem.utils.TitlePopup;

import static android.view.View.*;


public class MainActivity extends BaseActivity {

    private Button btnAuto, btnSaveStatusData;
    private ImageView ivRtk, ivLeftDown, ivRightDown, ivLeftUp, ivRightUp, ivClose;
    private TextView  tvSatelliteNum, tvRtk, tvDelay, tvNetwork, tvArea, tvSpeed, tvBaseHigh, tvRtkBar, tvSN, tvWorkMode, tvTime;
    private TextView  tvLeftDown, tvLeftUp, tvRightDown, tvRightUp, tvLeftHighDiff, tvRightHighDiff;
    private TextView  tvStatusLat, tvStatusLng, tvStatusHeight, tvStatusX, tvStatusY, tvStatusYaw, tvStatusRoll, tvStatusPitch;
    private LinearLayout btnSetting, btnStatus, btnThreeD, btnUp, btnPlus, btnBaseHigh, btnMinus, btnDown;
    private LayoutParams lpLeftDown, lpLeftUp, lpRightDown, lpRightUp;
    private FrameLayout flRtkStatus, flStatusData;

    private long mExitTime;

//    private TitlePopup titlePopup;

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateViewModule() {
        findViewById();
        setInnerOnClickListener();
//        initPopupView();
//        setInnerPopupView();
    }

    @Override
    protected void newExternalRelations() {
        new MainRelations(this);
    }

    private void setInnerOnClickListener() {
        ivClose.setOnClickListener((v) -> {
            flStatusData.setVisibility(INVISIBLE);
        });
    }

    private void findViewById(){
        ivRtk = findViewById(R.id.iv_rtk);
        flRtkStatus = findViewById(R.id.fl_rtk_status);
        flStatusData = findViewById(R.id.fl_status_data);
        ivLeftDown = findViewById(R.id.iv_left_down);
        ivLeftUp = findViewById(R.id.iv_left_up);
        ivRightDown = findViewById(R.id.iv_right_down);
        ivRightUp = findViewById(R.id.iv_right_up);
        ivClose = findViewById(R.id.iv_close);
        tvSatelliteNum = findViewById(R.id.tv_satellite_num);
        tvRtk = findViewById(R.id.tv_rtk);
        tvRtkBar = findViewById(R.id.tv_rtk_status);
        tvDelay = findViewById(R.id.tv_delay_value);
        tvNetwork = findViewById(R.id.tv_network);
        tvArea = findViewById(R.id.tv_area);
        tvSpeed = findViewById(R.id.tv_speed);
        tvBaseHigh = findViewById(R.id.tv_base_high);
        tvLeftDown = findViewById(R.id.tv_left_high_down);
        tvLeftUp = findViewById(R.id.tv_left_high_up);
        tvRightDown = findViewById(R.id.tv_right_high_down);
        tvRightUp = findViewById(R.id.tv_right_high_up);
        tvLeftHighDiff = findViewById(R.id.tv_left_high_diff);
        tvRightHighDiff = findViewById(R.id.tv_right_high_diff);
        tvSN = findViewById(R.id.tv_sn);
        tvWorkMode = findViewById(R.id.tv_work_mode);
        tvTime = findViewById(R.id.tv_time);
        tvStatusLat = findViewById(R.id.tv_status_lat);
        tvStatusLng = findViewById(R.id.tv_status_lng);
        tvStatusHeight= findViewById(R.id.tv_status_height);
        tvStatusX = findViewById(R.id.tv_status_x);
        tvStatusY = findViewById(R.id.tv_status_y);
        tvStatusYaw = findViewById(R.id.tv_status_yaw);
        tvStatusRoll = findViewById(R.id.tv_status_roll);
        tvStatusPitch = findViewById(R.id.tv_status_pitch);
        btnAuto = findViewById(R.id.btn_auto);
        btnSaveStatusData = findViewById(R.id.btn_save_status_data);
        btnUp = findViewById(R.id.ll_btn_up);
        btnDown = findViewById(R.id.ll_btn_down);
        btnPlus = findViewById(R.id.ll_btn_plus);
        btnMinus = findViewById(R.id.ll_btn_minus);
        btnBaseHigh = findViewById(R.id.ll_btn_base_high);
        btnSetting = findViewById(R.id.ll_btn_setting);
        btnStatus = findViewById(R.id.ll_btn_status);
        btnThreeD = findViewById(R.id.ll_btn_3d);
        lpLeftDown = ivLeftDown.getLayoutParams();
        lpLeftUp = ivLeftUp.getLayoutParams();
        lpRightDown = ivRightDown.getLayoutParams();
        lpRightUp = ivRightUp.getLayoutParams();
    }

    //显示卫星数
    public void setTvSatelliteNum(int satelliteNum) {
        tvSatelliteNum.setText(satelliteNum + "");
    }
    //显示rtk警报状态
    public void setRtkWarning(int rtkStatus){
        if (rtkStatus == 4) {
            ivRtk.setImageDrawable(getResources().getDrawable(R.mipmap.location_green));
            tvRtk.setText("固定");
            tvRtk.setTextColor(getResources().getColor(R.color.skisteergreen));
            flRtkStatus.setBackgroundColor(getResources().getColor(R.color.skisteergreen));
            tvRtkBar.setText("差分正常，可以正常工作");
            tvRtkBar.setTextColor(getResources().getColor(R.color.black));
            AnimationVisible.showAndHiddenAnimation(flRtkStatus,
                    AnimationVisible.AnimationState.STATE_HIDDEN,
                    0);
        } else if (rtkStatus == 5) {
            ivRtk.setImageDrawable(getResources().getDrawable(R.mipmap.location_yellow));
            tvRtk.setText("浮动");
            tvRtk.setTextColor(getResources().getColor(R.color.yellow));
            flRtkStatus.setVisibility(VISIBLE);
            flRtkStatus.setBackgroundColor(getResources().getColor(R.color.yellow));
            tvRtkBar.setText("差分浮动，请等待...");
            tvRtkBar.setTextColor(getResources().getColor(R.color.black));
        } else {
            ivRtk.setImageDrawable(getResources().getDrawable(R.mipmap.location_red));
            tvRtk.setText("单点");
            tvRtk.setTextColor(getResources().getColor(R.color.tomato));
            flRtkStatus.setVisibility(VISIBLE);
            flRtkStatus.setBackgroundColor(getResources().getColor(R.color.tomato));
            tvRtkBar.setText("差分异常，不可以正常工作");
            tvRtkBar.setTextColor(getResources().getColor(R.color.black));
        }
    }
    //显示差分延时
    public void setTvDelay(float delay) {
        tvDelay.setText(delay + "");
    }
    //显示网络状态
    public void setTvNetwork(int network) {
        if (network > -85) {
            tvNetwork.setText("信号极好");
        } else if (network > -95 && network <= -85) {
            tvNetwork.setText("信号很好");
        } else if (network > -105 && network <= -95) {
            tvNetwork.setText("信号中等");
        } else if (network > -115 && network <= -105) {
            tvNetwork.setText("信号较差");
        } else if (network < -115) {
            tvNetwork.setText("信号极差");
        } else {
            tvNetwork.setText("无网络");
        }
    }
    //显示作业面积
    public void setTvArea(float area) {
        tvArea.setText(area + "");
    }
    //显示作业速度
    public void setTvSpeed(float speed) {
        tvSpeed.setText(speed + "");
    }
    //显示主机号
    public void setTvSN(String sn) {
        tvSN.setText(sn);
    }
    //显示作业类型
    public void setTvWorkMode(boolean workMode) {
        if (workMode) tvWorkMode.setText("旱平地");
        else tvWorkMode.setText("水田平地");
    }
    //显示状态数据
    public void setFlStatusDataVisibility(boolean view) {
        if (view) {
            flStatusData.setVisibility(VISIBLE);
        } else {
            flStatusData.setVisibility(INVISIBLE);
        }
    }
    //显示时间
    public void setTvTime(String time) {
        tvTime.setText(time);
    }
    //显示经纬度高程值
    public void setTvGpsData(double lat, double lng, float height, float yaw) {
        tvStatusLat.setText(lat + "");
        tvStatusLng.setText(lng + "");
        tvStatusHeight.setText(height + "");
        tvStatusYaw.setText(yaw + "");
    }
    //显示东北天坐标
    public void setTvXY(String x, String y) {
        tvStatusX.setText(x);
        tvStatusY.setText(y);
    }
    //显示姿态传感器值
    public void setTvAhrsData(String roll, String pitch) {
        tvStatusRoll.setText(roll);
        tvStatusPitch.setText(pitch);
    }
    //显示
    //上升下降按键颜色
    public void setLlBtnUpPress() {
        btnUp.setBackground(getResources().getDrawable(R.drawable.button_pressed));
    }
    public void setLlBtnUpNormal() {
        btnUp.setBackground(getResources().getDrawable(R.drawable.button_normal));
    }
    public void setLlBtnDownPress() {
        btnDown.setBackground(getResources().getDrawable(R.drawable.button_pressed));
    }
    public void setLlBtnDownNormal() {
        btnDown.setBackground(getResources().getDrawable(R.drawable.button_normal));
    }
    //自动控制按钮颜色显示
    public void setTvAutoContent(boolean flag) {
        if (flag) {
            btnAuto.setText("结束平地");
            btnAuto.setBackground(getResources().getDrawable(R.drawable.shape_pressed));
        } else {
            btnAuto.setText("自动平地");
            btnAuto.setBackground(getResources().getDrawable(R.drawable.shape_normal));
        }
    }

    //显示左边高程相关
    public void setTvLeftPart(int highDiff, String high, float heightDead) {
        tvLeftHighDiff.setText(highDiff + "");
        if (highDiff > heightDead) {
            ivLeftDown.setVisibility(VISIBLE);
            ivLeftUp.setVisibility(INVISIBLE);
            tvLeftDown.setVisibility(VISIBLE);
            tvLeftUp.setVisibility(INVISIBLE);
            tvLeftDown.setText(high + "m");
            tvLeftHighDiff.setBackground(getResources().getDrawable(R.drawable.redframe_bg));
            tvLeftHighDiff.setTextColor(getResources().getColor(R.color.red));
        } else if (highDiff < -heightDead) {
            ivLeftDown.setVisibility(INVISIBLE);
            ivLeftUp.setVisibility(VISIBLE);
            tvLeftDown.setVisibility(INVISIBLE);
            tvLeftUp.setVisibility(VISIBLE);
            tvLeftUp.setText(high + "m");
            tvLeftHighDiff.setBackground(getResources().getDrawable(R.drawable.blueframe_bg));
            tvLeftHighDiff.setTextColor(getResources().getColor(R.color.umeng_fb_color_btn_pressed));
        } else {
            ivLeftDown.setVisibility(GONE);
            ivLeftUp.setVisibility(GONE);
            tvLeftDown.setVisibility(VISIBLE);
            tvLeftUp.setVisibility(GONE);
            tvLeftDown.setText(high + "m");
            tvLeftHighDiff.setBackground(getResources().getDrawable(R.drawable.grayframe_bg));
            tvLeftHighDiff.setTextColor(getResources().getColor(R.color.color_green));
        }

        if (highDiff < -(heightDead + 9)) {
            ivLeftUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up3));
            lpLeftUp.width = 60;
            lpLeftUp.height = 90;
            ivLeftUp.setLayoutParams(lpLeftUp);
        } else if (highDiff >= -(heightDead + 6) && highDiff < -(heightDead + 1)){
            ivLeftUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up2));
            lpLeftUp.width = 60;
            lpLeftUp.height = 60;
            ivLeftUp.setLayoutParams(lpLeftUp);
        } else if (highDiff >= -(heightDead + 3) && highDiff < -heightDead) {
            ivLeftUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up1));
            lpLeftUp.width = 60;
            lpLeftUp.height = 30;
            ivLeftUp.setLayoutParams(lpLeftUp);
        } else if (highDiff > heightDead && highDiff <= (heightDead + 3)) {
            ivLeftDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down1));
            lpLeftDown.width = 60;
            lpLeftDown.height = 30;
            ivLeftDown.setLayoutParams(lpLeftDown);
        } else if (highDiff > (heightDead + 1) && highDiff <= (heightDead + 6)) {
            ivLeftDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down2));
            lpLeftDown.width = 60;
            lpLeftDown.height = 60;
            ivLeftDown.setLayoutParams(lpLeftDown);
        } else if (highDiff > (heightDead + 9)) {
            ivLeftDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down3));
            lpLeftDown.width = 60;
            lpLeftDown.height = 90;
            ivLeftDown.setLayoutParams(lpLeftDown);
        }

    }

    //显示左边高程相关
    public void setTvRightPart(int highDiff, String high, float heightDead) {
        tvRightHighDiff.setText(highDiff + "");
        if (highDiff > heightDead) {
            ivRightDown.setVisibility(VISIBLE);
            ivRightUp.setVisibility(INVISIBLE);
            tvRightDown.setVisibility(VISIBLE);
            tvRightUp.setVisibility(INVISIBLE);
            tvRightDown.setText(high + "m");
            tvRightHighDiff.setBackground(getResources().getDrawable(R.drawable.redframe_bg));
            tvRightHighDiff.setTextColor(getResources().getColor(R.color.red));
        } else if (highDiff < -heightDead) {
            ivRightDown.setVisibility(INVISIBLE);
            ivRightUp.setVisibility(VISIBLE);
            tvRightDown.setVisibility(INVISIBLE);
            tvRightUp.setVisibility(VISIBLE);
            tvRightUp.setText(high + "m");
            tvRightHighDiff.setBackground(getResources().getDrawable(R.drawable.blueframe_bg));
            tvRightHighDiff.setTextColor(getResources().getColor(R.color.umeng_fb_color_btn_pressed));
        } else {
            ivRightDown.setVisibility(GONE);
            ivRightUp.setVisibility(GONE);
            tvRightDown.setVisibility(VISIBLE);
            tvRightUp.setVisibility(GONE);
            tvRightDown.setText(high + "m");
            tvRightHighDiff.setBackground(getResources().getDrawable(R.drawable.grayframe_bg));
            tvRightHighDiff.setTextColor(getResources().getColor(R.color.color_green));
        }

        if (highDiff < -(heightDead + 9)) {
            ivRightUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up3));
            lpRightUp.width = 60;
            lpRightUp.height = 90;
            ivRightUp.setLayoutParams(lpRightUp);
        } else if (highDiff >= -(heightDead + 6) && highDiff < -(heightDead + 1)){
            ivRightUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up2));
            lpRightUp.width = 60;
            lpRightUp.height = 60;
            ivRightUp.setLayoutParams(lpRightUp);
        } else if (highDiff >= -(heightDead + 3) && highDiff < -heightDead) {
            ivRightUp.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_up1));
            lpRightUp.width = 60;
            lpRightUp.height = 30;
            ivRightUp.setLayoutParams(lpRightUp);
        } else if (highDiff > heightDead && highDiff <= (heightDead + 3)) {
            ivRightDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down1));
            lpRightDown.width = 60;
            lpRightDown.height = 30;
            ivRightDown.setLayoutParams(lpRightDown);
        } else if (highDiff > (heightDead + 1) && highDiff <= (heightDead + 6)) {
            ivRightDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down2));
            lpRightDown.width = 60;
            lpRightDown.height = 60;
            ivRightDown.setLayoutParams(lpRightDown);
        } else if (highDiff > (heightDead + 9)) {
            ivRightDown.setImageDrawable(getResources().getDrawable(R.mipmap.triangle_down3));
            lpRightDown.width = 60;
            lpRightDown.height = 90;
            ivRightDown.setLayoutParams(lpRightDown);
        }
    }

    public void setBaseHeightPressView() {
        btnBaseHigh.setBackgroundColor(getResources().getColor(R.color.color_yellow));
    }

    public void setBaseHeightNormalView() {
        btnBaseHigh.setBackgroundColor(getResources().getColor(R.color.color_green));
    }

    public void setBaseHeightView(String baseHeight) {
        tvBaseHigh.setText(baseHeight);
    }

    public void setBtnSetting(OnClickListener setting) {
        btnSetting.setOnClickListener(setting);
    }

    public void setBtnAuto(OnClickListener auto){
        btnAuto.setOnClickListener(auto);
    }

    public void setBtnSaveStatusData(OnClickListener saveStatusData) {
        btnSaveStatusData.setOnClickListener(saveStatusData);
    }

    public void setBtnShortUp(OnTouchListener up){
        btnUp.setOnTouchListener(up);
    }

    public void setBtnShortDown(OnTouchListener down){
        btnDown.setOnTouchListener(down);
    }

    public void setBtnPlus(OnClickListener plus){
        btnPlus.setOnClickListener(plus);
    }

    public void setBtnMinus(OnClickListener minus){
        btnMinus.setOnClickListener(minus);
    }

    public void setBtnBaseHigh(OnLongClickListener baseHigh){
        btnBaseHigh.setOnLongClickListener(baseHigh);
    }

    public void setBtnStatus(OnClickListener status){
        btnStatus.setOnClickListener(status);
    }

    public void setBtnThreeD(OnClickListener threeD){
        btnThreeD.setOnClickListener(threeD);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击两次返回退出app
     **/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime) > 2000){
                showToast("再按一次退出APP");
                mExitTime = System.currentTimeMillis();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    //popup点击时间
//    private void setInnerPopupView(){
//        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
//            @Override
//            public void onItemClick(ActionItem item, int position) {
//                if (position == 0) {
//                    dialogMachineParamSetting();
//                } else if (position == 1) {
//
//                } else if (position == 2) {
//                    startActivity(new Intent(MainActivity.this, BaseStationActivity.class));
//                } else if (position == 3) {
//
//                }
//            }
//        });
//    }
//
//    //初始化popup组件
//    private void initPopupView() {
//        titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        //实例化标题栏按钮并设置监听
//        btnSetting.setOnClickListener((v) -> {
//            //弹出微信样式的选择对话框
//            titlePopup.show(v);
//        });
//        //初始化标题栏中的项目列表
//        titlePopup.addAction(new ActionItem(this, "机具参数", R.mipmap.workset));
//        titlePopup.addAction(new ActionItem(this, "控制参数", R.mipmap.controller));
//        titlePopup.addAction(new ActionItem(this, "基站配置", R.mipmap.base_station));
//        titlePopup.addAction(new ActionItem(this, "系统日志", R.mipmap.log));
//    }


}