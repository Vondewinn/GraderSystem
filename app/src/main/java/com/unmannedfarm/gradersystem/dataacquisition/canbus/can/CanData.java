package com.unmannedfarm.gradersystem.dataacquisition.canbus.can;

import android.util.Log;

import com.unmannedfarm.gradersystem.dataprocessing.DataBean;
import com.unmannedfarm.gradersystem.utils.DataUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  Vondewinn
 * @date   :  2022/03/06/18
 * @update :  2022
 * */

public class CanData implements DataBean {

    private byte[] receiveCanData = new byte[1024];
    private static byte[] canDataList    = new byte[1024];
    private static ArrayList<Long> canIdList = new ArrayList<>();
    private byte canData0 = 0;
    private byte canData1 = 0;
    private byte canData2 = 0;
    private byte canData3 = 0;
    private byte canData4 = 0;
    private byte canData5 = 0;
    private byte canData6 = 0;
    private byte canData7 = 0;


    public CanData() {

    }

    public void setReceiveCanData(byte[] receiveCanData) {
        this.receiveCanData = receiveCanData;
        /*********************新增************************/
        byte[] id = new byte[4];
        System.arraycopy(receiveCanData, 1, id, 0, id.length);
        byte[] data = null;
        int frameFormatType = id[3] & 0x06;
        int frameFormat = 0;
        int frameType = 0;
        long extId = 0;

        switch (frameFormatType) {
            case 0:
                frameFormat = 0;
                frameType = 0;
                extId = (((((id[0] & 0xff) << 24) | ((id[1] & 0xff) << 16) | ((id[2] & 0xff) << 8) | ((id[3] & 0xff))) & 0xFFFFFFFFl) >> 21);
                int dataLength = receiveCanData[5];
                data = new byte[dataLength];
                System.arraycopy(receiveCanData, 6, data, 0, dataLength);
                break;
            case 2:
                frameFormat = 0;
                frameType = 1;
                extId = (((((id[0] & 0xff) << 24) | ((id[1] & 0xff) << 16) | ((id[2] & 0xff) << 8) | ((id[3] & 0xff))) & 0xFFFFFFFFl) >> 21);
                break;
            case 4:
                frameFormat = 1;
                frameType = 0;
                extId = (((((id[0] & 0xff) << 24) | ((id[1] & 0xff) << 16) | ((id[2] & 0xff) << 8) | ((id[3] & 0xff))) & 0xFFFFFFFFl) >> 3);
                int dataLengthExtra = receiveCanData[5];
                data = new byte[dataLengthExtra];
                System.arraycopy(receiveCanData, 6, data, 0, dataLengthExtra);
                break;
            case 6:
                frameFormat = 1;
                frameType = 1;
                extId = (((((id[0] & 0xff) << 24) | ((id[1] & 0xff) << 16) | ((id[2] & 0xff) << 8) | ((id[3] & 0xff))) & 0xFFFFFFFFl) >> 3);
                break;
        }

        int dataLength = receiveCanData[5];

        if (dataLength == 1) {
            this.canData0 = data[0];
            this.canData1 = 0;
            this.canData2 = 0;
            this.canData3 = 0;
            this.canData4 = 0;
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 2) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = 0;
            this.canData3 = 0;
            this.canData4 = 0;
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 3) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = 0;
            this.canData4 = 0;
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 4) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = data[3];
            this.canData4 = 0;
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 5) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = data[3];
            this.canData4 = data[4];
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 6) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = data[3];
            this.canData4 = data[4];
            this.canData5 = data[5];
            this.canData6 = 0;
            this.canData7 = 0;
        } else if (dataLength == 7) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = data[3];
            this.canData4 = data[4];
            this.canData5 = data[5];
            this.canData6 = data[6];
            this.canData7 = 0;
        } else if (dataLength == 8) {
            this.canData0 = data[0];
            this.canData1 = data[1];
            this.canData2 = data[2];
            this.canData3 = data[3];
            this.canData4 = data[4];
            this.canData5 = data[5];
            this.canData6 = data[6];
            this.canData7 = data[7];
        }else {
            this.canData0 = 0;
            this.canData1 = 0;
            this.canData2 = 0;
            this.canData3 = 0;
            this.canData4 = 0;
            this.canData5 = 0;
            this.canData6 = 0;
            this.canData7 = 0;
        }

        if (canIdList.contains(extId)) {
            int dataPosition = canIdList.indexOf(extId);
            canDataList[(dataPosition*8)]   = this.canData0;
            canDataList[(dataPosition*8)+1] = this.canData1;
            canDataList[(dataPosition*8)+2] = this.canData2;
            canDataList[(dataPosition*8)+3] = this.canData3;
            canDataList[(dataPosition*8)+4] = this.canData4;
            canDataList[(dataPosition*8)+5] = this.canData5;
            canDataList[(dataPosition*8)+6] = this.canData6;
            canDataList[(dataPosition*8)+7] = this.canData7;
        } else {
            canIdList.add(extId);
            int dataPosition = canIdList.indexOf(extId);
            canDataList[(dataPosition*8)]   = this.canData0;
            canDataList[(dataPosition*8)+1] = this.canData1;
            canDataList[(dataPosition*8)+2] = this.canData2;
            canDataList[(dataPosition*8)+3] = this.canData3;
            canDataList[(dataPosition*8)+4] = this.canData4;
            canDataList[(dataPosition*8)+5] = this.canData5;
            canDataList[(dataPosition*8)+6] = this.canData6;
            canDataList[(dataPosition*8)+7] = this.canData7;
        }

    }

    public static byte[] getCanData(long canId){
        byte[] canData = new byte[8];
        if(canIdList.contains(canId)){
            int position = canIdList.indexOf(canId);
            canData[0] = canDataList[(position*8)];
            canData[1] = canDataList[(position*8)+1];
            canData[2] = canDataList[(position*8)+2];
            canData[3] = canDataList[(position*8)+3];
            canData[4] = canDataList[(position*8)+4];
            canData[5] = canDataList[(position*8)+5];
            canData[6] = canDataList[(position*8)+6];
            canData[7] = canDataList[(position*8)+7];
        }

        return canData;
    }

    public byte[] getReceiveCanData() {
        return receiveCanData;
    }

}
