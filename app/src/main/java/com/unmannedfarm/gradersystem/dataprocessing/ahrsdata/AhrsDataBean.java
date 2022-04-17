package com.unmannedfarm.gradersystem.dataprocessing.ahrsdata;

import com.unmannedfarm.gradersystem.dataprocessing.DataBean;

public class AhrsDataBean implements DataBean {

    private float roll = 0;
    private float pitch = 0;
    private float yaw = 0;
    private float gyroX = 0;
    private float gyroY = 0;
    private float gyroZ = 0;
    private float ax = 0;
    private float ay = 0;
    private float az = 0;

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getGyroX() {
        return gyroX;
    }

    public void setGyroX(float gyroX) {
        this.gyroX = gyroX;
    }

    public float getGyroY() {
        return gyroY;
    }

    public void setGyroY(float gyroY) {
        this.gyroY = gyroY;
    }

    public float getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(float gyroZ) {
        this.gyroZ = gyroZ;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }

    public float getAz() {
        return az;
    }

    public void setAz(float az) {
        this.az = az;
    }

    private float roll1 = 0;
    private float pitch1 = 0;
    private float yaw1 = 0;
    private float roll2 = 0;
    private float pitch2 = 0;
    private float yaw2 = 0;
    private float roll3 = 0;
    private float pitch3 = 0;
    private float yaw3 = 0;

    public float getRoll1() {
        return roll1;
    }

    public void setRoll1(float roll1) {
        this.roll1 = roll1;
    }

    public float getPitch1() {
        return pitch1;
    }

    public void setPitch1(float pitch1) {
        this.pitch1 = pitch1;
    }

    public float getYaw1() {
        return yaw1;
    }

    public void setYaw1(float yaw1) {
        this.yaw1 = yaw1;
    }

    public float getRoll2() {
        return roll2;
    }

    public void setRoll2(float roll2) {
        this.roll2 = roll2;
    }

    public float getPitch2() {
        return pitch2;
    }

    public void setPitch2(float pitch2) {
        this.pitch2 = pitch2;
    }

    public float getYaw2() {
        return yaw2;
    }

    public void setYaw2(float yaw2) {
        this.yaw2 = yaw2;
    }

    public float getRoll3() {
        return roll3;
    }

    public void setRoll3(float roll3) {
        this.roll3 = roll3;
    }

    public float getPitch3() {
        return pitch3;
    }

    public void setPitch3(float pitch3) {
        this.pitch3 = pitch3;
    }

    public float getYaw3() {
        return yaw3;
    }

    public void setYaw3(float yaw3) {
        this.yaw3 = yaw3;
    }

    @Override
    public String toString() {
        return "AhrsDataBean{" +
                "roll=" + roll +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", roll1=" + roll1 +
                ", pitch1=" + pitch1 +
                ", yaw1=" + yaw1 +
                ", roll2=" + roll2 +
                ", pitch2=" + pitch2 +
                ", yaw2=" + yaw2 +
                ", roll3=" + roll3 +
                ", pitch3=" + pitch3 +
                ", yaw3=" + yaw3 +
                '}';
    }
}
