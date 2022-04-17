package com.unmannedfarm.gradersystem.dataacquisition.canbus.can;

import com.unmannedfarm.gradersystem.dataacquisition.canbus.guard.FrameFormat;

public interface CANBus {

    void open();
    void close();
    void sendCommand(byte[] data);
    void sendCanData(int id, byte[] data, FrameFormat frameFormat);
    void dataReceiveBuffer();

}
