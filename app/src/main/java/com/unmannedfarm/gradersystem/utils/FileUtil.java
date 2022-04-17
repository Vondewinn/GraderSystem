package com.unmannedfarm.gradersystem.utils;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {

    /**
     * 创建了父目录就可以直接写入数据到子文件
     */
    public static void createFileIfNotExist(String filename) {
        File file = new File(filename);
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return;
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  删除文件中的某一行
     **/
    public static void removeLine(final File file, final int lineIndex) throws IOException {
        final List<String> lines = new LinkedList<>();
        final Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
        while(reader.hasNextLine())
            lines.add(reader.nextLine());
        reader.close();
        assert lineIndex >= 0 && lineIndex <= lines.size() - 1;
        lines.remove(lineIndex);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for(final String line : lines)
            writer.write(line);
        writer.flush();
        writer.close();
    }

    /**
     * 创建文件夹
     * */
    public static void createFolder(Context context, String folderName){
        String filePath = context.getExternalFilesDir(null) + "/" + folderName + "/";
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     *  获取文件中包含某部分特征的文件名或者文件夹名
     **/
    public static ArrayList<String> getFileName(Context context, String path, String namePart){
        String filePath = context.getExternalFilesDir(null) + path + ""; //获取路径
        File[] files = new File(filePath).listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String filename = file.getName();
                if (filename.contains(namePart)) {
                    fileList.add(filename);
                }
            }
        }
        return fileList;
    }

    /**
     *  获取文件中包含某部分特征的文件名或者文件夹名
     **/
    public static ArrayList<String> getAllFileName(Context context){
        String filePath = context.getExternalFilesDir(null) + ""; //获取路径
        File[] files = new File(filePath).listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String filename = file.getName();
                fileList.add(filename);
            }
        }
        return fileList;
    }

}
