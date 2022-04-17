package com.unmannedfarm.gradersystem.dataacquisition.canbus.can;

import android.content.Context;
import android.util.Log;

import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.CommunicationService;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.DataType;
import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.FrameFormat;

import org.greenrobot.eventbus.EventBus;


/**
 * @author Vondewinn
 * @date 2022/03/06/18
 * @update 2022
 * */

public class CAN{

    private final String TAG = getClass().getSimpleName();

    private static CommunicationService mService;
    private static Context mContext;
    private CanData canData = new CanData();
    private static CAN canBus;

    private CAN() {

    }

    public static CAN getCanInstance(Context context) {
        if (canBus == null) {
            canBus = new CAN();
            mContext = context;
        }
        return canBus;
    }

    /*****************new source********************/


    /**
     * fun: open CAN bus
     * */
    public void open(){
        try {
            mService = CommunicationService.getInstance(mContext);
            mService.setShutdownCountTime(12);
            mService.bind();
            //sendCommand(Command.Send.Channel1());   //默认是通道1
            //sendCommand(Command.Send.Switch250K()); //默认设置波特率为250K
            dataReceiveBuffer();
            Log.e(TAG, "CAN START!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * @fun : 命令设置方法
     * */
    public void sendCommand(byte[] data){
        if (mService != null) {
            if (mService.isBindSuccess()) {
                mService.send(data);
            }
        }
    }

    /**
     * @fun    : 发送数据
     * @param  : stdFormat为标准帧， extFormat为扩展帧
     * @notes  : 默认为通道1发送
     * */
    public static void sendCanData(int id, byte[] data, FrameFormat frameFormat) {
        if (mService != null) {
            if (mService.isBindSuccess()) {
                mService.sendCan((byte) 0x01, id, data, frameFormat);
            }
        }
    }

    /**
     * @fun: 私有数据接收缓冲区， 用以实时接收数据
     * */
    public void dataReceiveBuffer() {
        if (mService != null) {
            mService.getData(new CommunicationService.IProcessData() {
                @Override
                public void process(byte[] data, DataType type) {
                    switch (type){
                        case TDataCan:
                            canData.setReceiveCanData(data);
                            EventBus.getDefault().post(canData);
                            break;
                    }
                }
            });

        }
    }

    public void close() {
        if (mService != null) {
            try {
                mService.unbind();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /************************************Vondewinn*************************************/

}
