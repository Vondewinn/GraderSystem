package com.unmannedfarm.gradersystem.dataprocessing.ahrsdata;

import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CanData;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CanDataUtils;
import com.unmannedfarm.gradersystem.dataprocessing.DataBean;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessing;
import com.unmannedfarm.gradersystem.utils.DataUtils;

public class AhrsDataProcessing implements DataProcessing {

    private int canId;
    private int[] canDataArray = new int[40];
    private int num = 0;
    private int nextState = 0;
    private int startByte = 0;
    private float kAngle = 180;
    private float kGyro = 2000;
    private float kAcc = 16;
    private boolean aFlag = false;
    private boolean bFlag = false;
    private boolean cFlag = false;
    private boolean dFlag = false;
    private boolean startFlag = false;

    private AhrsDataBean ahrsDataBean = new AhrsDataBean();

    private int frameState = 0;
    private int byteNum    = 0;
    private int checkSum   = 0;
    private int[] ACCData   = new int[8];
    private int[] GYROData  = new int[8];
    private int[] AngleData = new int[8];

    public AhrsDataProcessing(int canId) {
        this.canId = canId;
    }


    /**
     * 涂师兄使用
     * */

    @Override
    public DataBean dataAnalysis(DataBean dataBean) {
        //数据获取
        CanData canData = (CanData) dataBean;
        byte[] ahrsData = canData.getReceiveCanData();
        //数据解析
        int[] canInitDataArray = CanDataUtils.handleCanData(ahrsData);

        if (canInitDataArray[0] == this.canId) {
            int data = canInitDataArray[2];
            int dataLength = 8;

            if (data == 0x51 && nextState == 0) {
                System.arraycopy(canInitDataArray, 1, canDataArray, num, dataLength);
                num += 8;
                nextState = 1;
            } else if (data == 0x52 && nextState == 1) {
                System.arraycopy(canInitDataArray, 1, canDataArray, num, dataLength);
                num += 8;
                nextState = 2;
            } else if (data == 0x53 && nextState == 2) {
                System.arraycopy(canInitDataArray, 1, canDataArray, num, dataLength);
                num = 0;
                nextState = 0;
                aFlag = true;
            }
        }

        if (aFlag) {
            if (canDataArray[0] == 0x55 && canDataArray[1] == 0x51) {
                float ax = (float) ((canDataArray[3] << 8) | canDataArray[2]) / 32768 * kAcc;
                float ay = (float) ((canDataArray[5] << 8) | canDataArray[4]) / 32768 * kAcc;
                float az = (float) ((canDataArray[7] << 8) | canDataArray[6]) / 32768 * kAcc;
                if (ax >= kAcc) ax -= 2 * kAcc;
                if (ay >= kAcc) ay -= 2 * kAcc;
                if (az >= kAcc) az -= 2 * kAcc;
                ahrsDataBean.setAx(ax);
                ahrsDataBean.setAy(ay);
                ahrsDataBean.setAz(az);
            } else {
                ahrsDataBean.setAx(0);
                ahrsDataBean.setAy(0);
                ahrsDataBean.setAz(0);
            }

            if (canDataArray[0 + 8] == 0x55 && canDataArray[1 + 8] == 0x52) {
                float gyroX = (float) ((canDataArray[3 + 8] << 8) | canDataArray[2 + 8]) / 32768 * kGyro;
                float gyroY = (float) ((canDataArray[5 + 8] << 8) | canDataArray[4 + 8]) / 32768 * kGyro;
                float gyroZ = (float) ((canDataArray[7 + 8] << 8) | canDataArray[6 + 8]) / 32768 * kGyro;
                if (gyroX >= kGyro) gyroX -= 2 * kGyro;
                if (gyroY >= kGyro) gyroY -= 2 * kGyro;
                if (gyroZ >= kGyro) gyroZ -= 2 * kGyro;
                ahrsDataBean.setGyroX(gyroX);
                ahrsDataBean.setGyroY(gyroY);
                ahrsDataBean.setGyroZ(gyroZ);
            } else {
                ahrsDataBean.setGyroX(0);
                ahrsDataBean.setGyroY(0);
                ahrsDataBean.setGyroZ(0);
            }

            if (canDataArray[0 + 16] == 0x55 && canDataArray[1 + 16] == 0x53) {
                float roll  = (float) ((canDataArray[3 + 16] << 8) | canDataArray[2 + 16]) / 32768 * kAngle;
                float pitch = (float) ((canDataArray[5 + 16] << 8) | canDataArray[4 + 16]) / 32768 * kAngle;
                float yaw   = (float) ((canDataArray[7 + 16] << 8) | canDataArray[6 + 16]) / 32768 * kAngle;
                if (roll > kAngle)  roll -= 2 * kAngle;
                if (pitch > kAngle) pitch -= 2 * kAngle;
                if (yaw > kAngle)   yaw -= 2 * kAngle;
                ahrsDataBean.setRoll(roll);
                ahrsDataBean.setPitch(pitch);
                ahrsDataBean.setYaw(yaw);
            } else {
                ahrsDataBean.setRoll(0);
                ahrsDataBean.setPitch(0);
                ahrsDataBean.setYaw(0);
            }
            aFlag = false;
            return ahrsDataBean;
        } else {
            return null;
        }

//        if (canInitDataArray[0] == 0x051) {
//            if (canInitDataArray[1] == 0x55 && canInitDataArray[2] == 0x53) {
//                float roll  = (float) ((canInitDataArray[4] << 8) | canInitDataArray[3]) / 32768 * kAngle;
//                float pitch = (float) ((canInitDataArray[6] << 8) | canInitDataArray[5]) / 32768 * kAngle;
//                float yaw   = (float) ((canInitDataArray[8] << 8) | canInitDataArray[7]) / 32768 * kAngle;
//                if (roll > 180)  roll -= 2 * kAngle;
//                if (pitch > 180) pitch -= 2 * kAngle;
//                if (yaw > 180)   yaw -= 2 * kAngle;
//                ahrsDataBean.setRoll1(roll);
//                ahrsDataBean.setPitch1(pitch);
//                ahrsDataBean.setYaw1(yaw);
//            } else {
//                ahrsDataBean.setRoll1(0);
//                ahrsDataBean.setPitch1(0);
//                ahrsDataBean.setYaw1(0);
//            }
//            return ahrsDataBean;
//        } else {
//            return null;
//        }

        /**
         * 靖怡使用
         * */

//        if (canInitDataArray[0] == 0x050) {
//            if (canInitDataArray[1] == 0x55 && canInitDataArray[2] == 0x53) {
//                float roll  = (float) ((canInitDataArray[4] << 8) | canInitDataArray[3]) / 32768 * kAngle;
//                float pitch = (float) ((canInitDataArray[6] << 8) | canInitDataArray[5]) / 32768 * kAngle;
//                float yaw   = (float) ((canInitDataArray[8] << 8) | canInitDataArray[7]) / 32768 * kAngle;
//                if (roll > 180)  roll -= 2 * kAngle;
//                if (pitch > 180) pitch -= 2 * kAngle;
//                if (yaw > 180)   yaw -= 2 * kAngle;
//                ahrsDataBean.setRoll(roll);
//                ahrsDataBean.setPitch(pitch);
//                ahrsDataBean.setYaw(yaw);
//            } else {
//                ahrsDataBean.setRoll(0);
//                ahrsDataBean.setPitch(0);
//                ahrsDataBean.setYaw(0);
//            }
//            aFlag = true;
//        } else if (canInitDataArray[0] == 0x051) {
//            if (canInitDataArray[1] == 0x55 && canInitDataArray[2] == 0x53) {
//                float roll  = (float) ((canInitDataArray[4] << 8) | canInitDataArray[3]) / 32768 * kAngle;
//                float pitch = (float) ((canInitDataArray[6] << 8) | canInitDataArray[5]) / 32768 * kAngle;
//                float yaw   = (float) ((canInitDataArray[8] << 8) | canInitDataArray[7]) / 32768 * kAngle;
//                if (roll > 180)  roll -= 2 * kAngle;
//                if (pitch > 180) pitch -= 2 * kAngle;
//                if (yaw > 180)   yaw -= 2 * kAngle;
//                ahrsDataBean.setRoll1(roll);
//                ahrsDataBean.setPitch1(pitch);
//                ahrsDataBean.setYaw1(yaw);
//            } else {
//                ahrsDataBean.setRoll1(0);
//                ahrsDataBean.setPitch1(0);
//                ahrsDataBean.setYaw1(0);
//            }
//            bFlag = true;
//        } else if (canInitDataArray[0] == 0x052) {
//            if (canInitDataArray[1] == 0x55 && canInitDataArray[2] == 0x53) {
//                float roll  = (float) ((canInitDataArray[4] << 8) | canInitDataArray[3]) / 32768 * kAngle;
//                float pitch = (float) ((canInitDataArray[6] << 8) | canInitDataArray[5]) / 32768 * kAngle;
//                float yaw   = (float) ((canInitDataArray[8] << 8) | canInitDataArray[7]) / 32768 * kAngle;
//                if (roll > 180)  roll -= 2 * kAngle;
//                if (pitch > 180) pitch -= 2 * kAngle;
//                if (yaw > 180)   yaw -= 2 * kAngle;
//                ahrsDataBean.setRoll2(roll);
//                ahrsDataBean.setPitch2(pitch);
//                ahrsDataBean.setYaw2(yaw);
//            } else {
//                ahrsDataBean.setRoll2(0);
//                ahrsDataBean.setPitch2(0);
//                ahrsDataBean.setYaw2(0);
//            }
//            cFlag = true;
//        } else if (canInitDataArray[0] == 0x053) {
//            if (canInitDataArray[1] == 0x55 && canInitDataArray[2] == 0x53) {
//                float roll  = (float) ((canInitDataArray[4] << 8) | canInitDataArray[3]) / 32768 * kAngle;
//                float pitch = (float) ((canInitDataArray[6] << 8) | canInitDataArray[5]) / 32768 * kAngle;
//                float yaw   = (float) ((canInitDataArray[8] << 8) | canInitDataArray[7]) / 32768 * kAngle;
//                if (roll > 180)  roll -= 2 * kAngle;
//                if (pitch > 180) pitch -= 2 * kAngle;
//                if (yaw > 180)   yaw -= 2 * kAngle;
//                ahrsDataBean.setRoll3(roll);
//                ahrsDataBean.setPitch3(pitch);
//                ahrsDataBean.setYaw3(yaw);
//            } else {
//                ahrsDataBean.setRoll3(0);
//                ahrsDataBean.setPitch3(0);
//                ahrsDataBean.setYaw3(0);
//            }
//            dFlag = true;
//        }
//
//        if (aFlag && bFlag && cFlag && dFlag) {
//            aFlag = false;
//            bFlag = false;
//            cFlag = false;
//            dFlag = false;
//            return ahrsDataBean;
//        } else {
//            return null;
//        }

//        if (canInitDataArray[0] == 0x050 || canInitDataArray[0] == 0x051) {
//            for (int i = 0; i < canDataArray.length; i++) {
//                int data = canInitDataArray[i + 1];
//                if (frameState == 0) {
//                    if (data == 0x55 && byteNum == 0) {
//                        checkSum = data;
//                        byteNum = 1;
//                    } else if (data == 0x51 && byteNum == 1) {
//                        checkSum += data;
//                        frameState = 2;
//                        byteNum = 2;
//                    } else if (data == 0x52 && byteNum == 1) {
//                        checkSum += data;
//                        frameState = 1;
//                        byteNum = 2;
//                    } else if (data == 0x53 && byteNum == 1) {
//                        checkSum += data;
//                        frameState = 3;
//                        byteNum = 2;
//                    }
//                } else if (frameState == 1) {
//                    if (byteNum < 10) {
//                        ACCData[byteNum - 2] = data;
//                        checkSum += data;
//                        byteNum += 1;
//                    } else {
//                        //if (data == (checkSum & 0xff)) {
//                            //todo 解析加速度数据
//                            float ax = (float) ((ACCData[1] << 8) | ACCData[0]) / 32768 * kAcc;
//                            float ay = (float) ((ACCData[3] << 8) | ACCData[2]) / 32768 * kAcc;
//                            float az = (float) ((ACCData[5] << 8) | ACCData[4]) / 32768 * kAcc;
//                            if (ax >= kAcc) ax -= 2 * kAcc;
//                            if (ay >= kAcc) ay -= 2 * kAcc;
//                            if (az >= kAcc) az -= 2 * kAcc;
//                            ahrsDataBean.setAx(ax);
//                            ahrsDataBean.setAy(ay);
//                            ahrsDataBean.setAz(az);
//                        //}
//                        checkSum = 0;
//                        byteNum = 0;
//                        frameState = 0;
//                    }
//                }  else if (frameState == 2) {
//                    if (byteNum < 10) {
//                        GYROData[byteNum - 2] = data;
//                        checkSum += data;
//                        byteNum += 1;
//                    } else {
//                        if (data == (checkSum & 0xff)) {
//                            //todo 解析gyro数据
//                        }
//                        checkSum = 0;
//                        byteNum = 0;
//                        frameState = 0;
//                    }
//                } else if (frameState == 3) {
//                    if (byteNum < 10) {
//                        AngleData[byteNum - 2] = data;
//                        checkSum += data;
//                        byteNum += 1;
//                    } else {
//                        //if (data == checkSum) {
//                            //todo 解析欧拉角数据
//                            float roll  = (float) ((AngleData[1] << 8) | AngleData[0]) / 32768 * kAngle;
//                            float pitch = (float) ((AngleData[3] << 8) | AngleData[2]) / 32768 * kAngle;
//                            float yaw   = (float) ((AngleData[5] << 8) | AngleData[4]) / 32768 * kAngle;
//                            if (roll > 180) roll -= 2 * kAngle;
//                            if (pitch > 180) pitch -= 2 * kAngle;
//                            if (yaw > 180)  yaw -= 2 * kAngle;
//                            ahrsDataBean.setRoll(roll);
//                            ahrsDataBean.setPitch(pitch);
//                            ahrsDataBean.setYaw(yaw);
//                        //}
//                        checkSum = 0;
//                        byteNum = 0;
//                        frameState = 0;
//                    }
//                }
//            }
//            for (int i = 0; i < canDataArray.length; i++) {
//                canDataArray[i] = 0;
//            }
//            return ahrsDataBean;
//        } else {
//            return null;
//        }

    }

    @Override
    public DataBean dataAnalysis(byte[] data) {
        return null;
    }

    @Override
    public DataBean dataAnalysis(String data) {
        return null;
    }

}
