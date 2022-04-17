package com.unmannedfarm.gradersystem.dataacquisition.serialport;

import com.unmannedfarm.gradersystem.dataprocessing.DataBean;

/**
 * @author Vondewinn
 * @date 2022/03/06/18
 * @update 2022
 * */

public class SerialPortData implements DataBean {

    private byte[] receiveData = new byte[1024];
    private String revStrData = new String();


    public String getRevStrData() {
        return revStrData;
    }

    public void setRevStrData(String revStrData) {
        this.revStrData = revStrData;
    }

    public byte[] getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(byte[] receiveData) {
        this.receiveData = receiveData;
    }
    
}
