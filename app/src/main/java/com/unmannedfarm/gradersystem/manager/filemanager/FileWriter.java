package com.unmannedfarm.gradersystem.manager.filemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {

    private long nDataLength = 0;
//    private static class SerialPortFileWriteHolder {
//        private static final FileWriter INSTANCE = new FileWriter();
//    }
    public FileWriter (){

    }
//    public static final FileWriter GetInstance() {
//        return SerialPortFileWriteHolder.INSTANCE;
//    }
    FileOutputStream mOutStream = null;
    private String mStrFileName = "";

    public void createFile(String strFileName, boolean append) {
        close();
        mStrFileName = strFileName;
        File newFile = new File(strFileName);

        if (newFile.exists()) {
            try {
                nDataLength = 0;
                //文件存在，往文件后面增加内容
                if (append == true) {
                    mOutStream = new FileOutputStream(newFile, true);
                } else{
                    mOutStream = new FileOutputStream(newFile, false);
                }
            } catch (FileNotFoundException e) {
            }
        }else {
            try {
                nDataLength = 0;
                mOutStream = new FileOutputStream(newFile);
            } catch (FileNotFoundException e) {

            }
        }

    }

    public void close() {
        if (mOutStream != null) {
            try {
                nDataLength = 0;
                mOutStream.close();
            } catch (IOException e) {
            }
            mOutStream = null;
        }
    }

    public boolean IsOpen() {
        return (mOutStream != null);
    }

    public void write(byte[] bytes, int nLength) {
        if (!IsOpen()) {
            if (!mStrFileName.isEmpty()) {
                File newFile = new File(mStrFileName);
                    if (newFile.exists()) {
                        try {
                            nDataLength = 0;
                            mOutStream = new FileOutputStream(newFile, true); //文件存在，往文件后面增加内容
                        } catch (FileNotFoundException e) {

                        }
                    }else {
                        createFile(mStrFileName, false);   //覆盖原来的文件
                    }
            }else {
                return;
            }
        }else {
            try {
                mOutStream.write(bytes, 0, nLength);
                nDataLength = nDataLength + nLength;
                //文件大于500M就停止写入
                if (nDataLength > 512000) {
                    close();
                }
            } catch (IOException e) {
                close();
            }
        }
    }

    public void writeToFile(String text){
        byte[] data = text.getBytes();
        int dataLength = data.length;
        if (mOutStream != null) {
            try {
                mOutStream.write(data, 0, dataLength);
                nDataLength = nDataLength + dataLength;
                //文件大于500M就停止写入
                if (nDataLength > 512000) {
                    close();
                }
            } catch (IOException e) {
                close();
            }
        }
    }

}








