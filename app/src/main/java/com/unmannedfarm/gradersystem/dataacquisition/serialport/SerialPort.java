package com.unmannedfarm.gradersystem.dataacquisition.serialport;

import android.util.Log;

import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataProcessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.stick.AbsStickPackageHelper;
import tp.xmaihh.serialport.stick.BaseStickPackageHelper;
import tp.xmaihh.serialport.utils.ByteUtil;

/**
 * @author Vondewinn
 * @date 2022-03-06
 * @update 2022
 * */

public abstract class SerialPort {

    /***********************new source***********************/
    private final String TAG = getClass().getSimpleName();
    private android_serialport_api.SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread readThread;
    private String sPort;
    private int iBaudRate;
    private int flag;
    private int stopBits = 1;
    private int dataBits = 8;
    private int parity = 0;
    private int flowCon = 0;
    private boolean _isOpen = false;

    public SerialPort(SerialPortSource source, BaudRate rate, int flag) {

        switch (source){
            case com0:{
                sPort = "/dev/ttyS0";
                break;
            }
            case com1:{
                sPort = "/dev/ttyS4";
                break;
            }
            case com2:{
                sPort = "/dev/ttyS6";
                break;
            }
        }

        switch (rate){
            case rate9600:{
                iBaudRate = 9600;
                break;
            }
            case rate57600:{
                iBaudRate = 57600;
                break;
            }
            case rate115200:{
                iBaudRate = 115200;
                break;
            }
        }

        this.flag = flag;
    }

    public void open() {
        try {
            this.mSerialPort = new android_serialport_api.SerialPort(new File(this.sPort), this.iBaudRate, this.stopBits,
                    this.dataBits, this.parity, this.flowCon, this.flag);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
            this.readThread = new ReadThread();
            this.readThread.start();
            this._isOpen = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void send(byte[] data) {
        if (data.length > 0) {
            try {
                this.mOutputStream.write(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendHex(String sHex) {
        byte[] bOutArray = ByteUtil.HexToByteArr(sHex);
        send(bOutArray);
    }

    public void sendText(String sText) {
        byte[] bOutArray = sText.getBytes();
        send(bOutArray);
    }

    private class ReadThread extends Thread {
        byte[] receiveDataBuffer = new byte[1024];
        private ReadThread() {
        }
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (SerialPort.this.mInputStream == null) {
                        return;
                    }

                    int byteRead = SerialPort.this.mInputStream.read(receiveDataBuffer);
                    if (receiveDataBuffer != null && byteRead > 0) {
                        ComBean ComRecData = new ComBean(SerialPort.this.sPort, receiveDataBuffer, byteRead);
                        SerialPort.this.onDataReceived(ComRecData);
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        try {
            if (this.mInputStream != null) {
                this.mInputStream.close();
                this.mInputStream = null;
            }
            if (this.mOutputStream != null) {
                this.mOutputStream.close();
                this.mOutputStream = null;
            }
            if (this.mSerialPort != null) {
                this.mSerialPort.close();
                this.mSerialPort = null;
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    protected abstract void onDataReceived(ComBean ComRecData);

}
