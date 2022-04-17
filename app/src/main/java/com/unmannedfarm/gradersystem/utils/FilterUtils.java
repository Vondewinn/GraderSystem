package com.unmannedfarm.gradersystem.utils;

public class FilterUtils {


    /**
     * @fun : 滑动滤波器
     * @param : 入口参数()
     * @author: Vondewinn
     * @date : 2022-03-18
     * */
    private static int dataCntI = 0, dataCntJ = 0;
    private static float[] dataSliding = new float[10];
    private static float height = 0;

    public static float averageFilter(int num, float data) {
        if (dataCntI < num) {
            dataSliding[dataCntI] = data;
            height = (dataSliding[dataCntI] + height) / (dataCntI + 1);
        } else {
            dataSliding[dataCntJ] = data;
            dataCntJ ++;
            if (dataCntJ == 9) dataCntJ = 0;
            for (int k = 0; k < num; k++) {
                height = dataSliding[k] + height;
            }
            height = height / (num + 1);
        }
        dataCntI ++;
        if (dataCntI == Integer.MAX_VALUE) dataCntI = num;
        return height;
    }

}
