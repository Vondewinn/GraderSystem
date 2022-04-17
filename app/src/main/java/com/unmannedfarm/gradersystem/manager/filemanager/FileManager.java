package com.unmannedfarm.gradersystem.manager.filemanager;


import com.unmannedfarm.gradersystem.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * 文件模块
 */

public class FileManager {

    private String mPath;
    private boolean mOpened;
    private BufferedWriter mWriter;

    public FileManager(String path) {
        mPath = path;
        mOpened = false;
    }

    public String getPath() {
        return mPath;
    }

    public boolean isOpened() {
        return mOpened;
    }

    public void open() {
        try {
            FileUtil.createFileIfNotExist(mPath);
            mWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(mPath, true), "UTF-8"));
            mOpened = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(String text) {
        try {
            mWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            mWriter.close();
            mOpened = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
