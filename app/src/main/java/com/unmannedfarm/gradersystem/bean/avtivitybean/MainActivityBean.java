package com.unmannedfarm.gradersystem.bean.avtivitybean;

import com.unmannedfarm.gradersystem.controller.gpscontroller.GraderType;

public class MainActivityBean {

    private GraderType graderType;      //平地类型
    private int rtkQuality;             //差分质量因子
    private float diffTimeout;          //差分延时
    private float heightDeadZoneValue;  //高程死区值
    private float rollDeadZoneValue;    //角度死区值
    private float averageHeight;        //平均高程值
    private float baseHeight;           //基准高程值
    private float machineWidth;         //机具幅宽
    private int controlSpeedLevel;    //控制速度
    private boolean startAutoFlag;      //开启自动平地标志位
    private boolean getBaseHeightFlag;  //获取基准高程标志位
    private boolean startSaveFlag;      //开始保存数据标志位

    public boolean isGetBaseHeightFlag() {
        return getBaseHeightFlag;
    }

    public void setGetBaseHeightFlag(boolean getBaseHeightFlag) {
        this.getBaseHeightFlag = getBaseHeightFlag;
    }

    public boolean isStartAutoFlag() {
        return startAutoFlag;
    }

    public void setStartAutoFlag(boolean startAutoFlag) {
        this.startAutoFlag = startAutoFlag;
    }

    public int getControlSpeedLevel() {
        return controlSpeedLevel;
    }

    public void setControlSpeedLevel(int controlSpeedLevel) {
        this.controlSpeedLevel = controlSpeedLevel;
    }

    public float getMachineWidth() {
        return machineWidth;
    }

    public void setMachineWidth(float antennaDistance) {
        this.machineWidth = antennaDistance;
    }

    public float getBaseHeight() {
        return baseHeight;
    }

    public void setBaseHeight(float baseHeight) {
        this.baseHeight = baseHeight;
    }

    public float getHeightDeadZoneValue() {
        return heightDeadZoneValue;
    }

    public void setHeightDeadZoneValue(float heightDeadZoneValue) {
        this.heightDeadZoneValue = heightDeadZoneValue;
    }

    public float getRollDeadZoneValue() {
        return rollDeadZoneValue;
    }

    public void setRollDeadZoneValue(float rollDeadZoneValue) {
        this.rollDeadZoneValue = rollDeadZoneValue;
    }

    public float getAverageHeight() {
        return averageHeight;
    }

    public void setAverageHeight(float averageHeight) {
        this.averageHeight = averageHeight;
    }

    public int getRtkQuality() {
        return rtkQuality;
    }

    public void setRtkQuality(int rtkQuality) {
        this.rtkQuality = rtkQuality;
    }

    public float getDiffTimeout() {
        return diffTimeout;
    }

    public void setDiffTimeout(float diffTimeout) {
        this.diffTimeout = diffTimeout;
    }

    public GraderType getGraderType() {
        return graderType;
    }

    public void setGraderType(GraderType graderType) {
        this.graderType = graderType;
    }

    public boolean isStartSaveFlag() {
        return startSaveFlag;
    }

    public void setStartSaveFlag(boolean startSaveFlag) {
        this.startSaveFlag = startSaveFlag;
    }
}
