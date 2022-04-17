package com.unmannedfarm.gradersystem.manager.ntripmanager;

public class GpsDifferentialData {

    private byte[] GpsDiffData = new byte[1024];

    public byte[] getGpsDiffData() {
        return GpsDiffData;
    }

    public void setGpsDiffData(byte[] gpsDiffData) {
        GpsDiffData = gpsDiffData;
    }
}
