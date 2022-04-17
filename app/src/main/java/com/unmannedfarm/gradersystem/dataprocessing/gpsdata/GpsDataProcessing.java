package com.unmannedfarm.gradersystem.dataprocessing.gpsdata;

import com.unmannedfarm.gradersystem.dataacquisition.serialport.SerialPortData;
import com.unmannedfarm.gradersystem.dataprocessing.DataBean;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessing;
import com.unmannedfarm.gradersystem.manager.ntripmanager.NetWorkServiceNtrip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * GPGGA原始格式为：
 * 格式示例：$GPGGA,014434.70,3817.13334637,N,12139.72994196,E,4,07,1.5,6.571,M,8.942,M,0.7,0016*79
 * 该数据帧的结构及各字段释义如下：
 * $GPGGA,<1>,<2>,<3>,<4>,<5>,<6>,<7>,<8>,<9>,M,<10>,M,<11>,<12>*xx<CR><LF>
 * @param :
 * <1:diff_timeout> <2:rtkValue> <3:SateNum>
 * */

/**
 * @fun    : GPS数据处理
 * @author : Vondewinn
 * @date   : 2022-03-08
 * @param  : 入口参数（gpsData）、输出参数（GpsDataBean）
 * */

public class GpsDataProcessing implements DataProcessing{

    private static String strData;
    private static String dataOut;

    public GpsDataProcessing() {

    }
    /**
     * @author : Vondewinn
     * @fun : GPS数据拼接
     * @date: 2022-03-12
     * @param : 返回完整的GPGGA，GPTRA，GPVTG数据包
     * */
    public static String gpsDataStitching(String dataPart) {
        String ggaHeader = "$GPGGA";
        int ggaLength = ggaHeader.length();
        strData = strData + dataPart;
        int s = strData.indexOf(ggaHeader);
        String subData = strData.substring(s + ggaLength);
        if (subData.contains(ggaHeader)){
            dataOut = strData.substring(s, subData.indexOf(ggaHeader) + ggaLength);
            strData = subData.substring(subData.indexOf(ggaHeader));
            return dataOut;
        } else {
            return null;
        }
    }


    /**
     * @author : Vondewinn
     * @fun : GPS数据解析
     * @date: 2022-03-12
     * @param : 返回GpsDataBean
     * */
    @Override
    public DataBean dataAnalysis(String gpsData){
        if(null == gpsData){
            return null;
        }
        GpsDataBean gpsDataBean = new GpsDataBean();
        int ggaLength = gpsData.indexOf("$GPGGA,");
        if(-1 != ggaLength) {
            String string = gpsData.substring(ggaLength);
            ggaLength = string.indexOf("\r\n");
            if(-1 != ggaLength){
                string = string.substring(0, ggaLength);
                NetWorkServiceNtrip.GGA = string;
                //System.out.println(string);
                String str = string.substring(0, string.length() - 1);
                List<String> gpsDataList = new ArrayList<>();
                String[] split = str.split(",");
                for (String s : split) {
                    gpsDataList.add(s);
                }
                if(gpsDataList.size() == 15) {

                    if (!gpsDataList.get(1).isEmpty()) {
                        gpsDataBean.setTime(gpsDataList.get(1));
                    }

                    if (!gpsDataList.get(13).isEmpty()) {
                        gpsDataBean.setDiffTimeout(Float.parseFloat(gpsDataList.get(13)));
                    } else {
                        gpsDataBean.setDiffTimeout(0f);
                    }
                    if (!gpsDataList.get(6).isEmpty()) {
                        gpsDataBean.setGpsRtkQuality(Integer.parseInt(gpsDataList.get(6)));
                    }
                    if (!gpsDataList.get(7).isEmpty()) {
                        gpsDataBean.setSatelliteNumber(Integer.parseInt(gpsDataList.get(7)));
                    }
                    if (!gpsDataList.get(9).isEmpty()) {
                        gpsDataBean.setHeightValue(Float.parseFloat(gpsDataList.get(9)));
                    }
                    if(gpsDataList.get(2).length() >= 12){
                        //纬度DDMM.MMMMMMM转为DDX.XXXXXXX
                        String wd_dd = gpsDataList.get(2).substring(0, 2);
                        String wd_mm_mmmmmmm = gpsDataList.get(2).substring(2, 11);
                        BigDecimal wd_miao = new BigDecimal(wd_mm_mmmmmmm);
                        BigDecimal v_60 = new BigDecimal("60");  //数值60
                        BigDecimal wd_miao_60 = wd_miao.divide(v_60, 8, BigDecimal.ROUND_HALF_UP);  //mm.mmmmmmm除以60，保留8位小数，四舍五入
                        String s_wd_miao = wd_miao_60.toPlainString();
                        String gpsLat = wd_dd + s_wd_miao.substring(1);
                        gpsDataBean.setGpsLat(Double.parseDouble(gpsLat));
                    }
                    if (gpsDataList.get(4).length() >= 13) {
                        //经度DDDMM.MMMMMMM转为DDDX.XXXXXXX
                        String jd_ddd = gpsDataList.get(4).substring(0, 3);
                        String jd_mm_mmmmmmm = gpsDataList.get(4).substring(3, 12);
                        BigDecimal jd_miao = new BigDecimal(jd_mm_mmmmmmm);
                        BigDecimal v_60 = new BigDecimal("60");  //数值60
                        BigDecimal jd_miao_60 = jd_miao.divide(v_60, 8, BigDecimal.ROUND_HALF_UP);  //mm.mmmmmmm除以60，保留8位小数，四舍五入
                        String s_jd_miao = jd_miao_60.toPlainString();
                        String gpsLng = jd_ddd + s_jd_miao.substring(1);
                        gpsDataBean.setGpsLng(Double.parseDouble(gpsLng));
                    }
                }
            }
        }
        //GPVTG
        int vtgLength = gpsData.indexOf("$GPVTG,");
        if(-1 != vtgLength){
            String string = gpsData.substring(vtgLength);
            vtgLength = string.indexOf("\r\n");
            if(-1 != vtgLength) {
                string = string.substring(0, vtgLength);
                //System.out.println(string);
                String str = string.substring(0, string.length() - 1);
                List<String> gpsDataList = new ArrayList<>();
                String[] split = str.split(",");
                for (String s : split) {
                    gpsDataList.add(s);
                }
                if(gpsDataList.size() == 10){
                    if(gpsDataList.get(7).length() > 0){
                        String speed = String.format("%.2f", Float.parseFloat(gpsDataList.get(7)));
                        gpsDataBean.setSpeed(Float.parseFloat(speed));
                    }
                }
            }
        }
        //GPTRA
        int traLength = gpsData.indexOf("$GPTRA,");
        if(-1 != traLength){
            String string = gpsData.substring(traLength);
            traLength = string.indexOf("\r\n");
            if(-1 != traLength) {
                string = string.substring(0, traLength);
                //System.out.println(string);
                String str = string.substring(0, string.length() - 1);
                List<String> gpsDataList = new ArrayList<>();
                String[] split = str.split(",");
                for (String s : split) {
                    gpsDataList.add(s);
                }
                if(gpsDataList.size() == 9) {
                    if(!gpsDataList.get(2).isEmpty()){
                        String yaw = String.format("%.2f", Float.parseFloat(gpsDataList.get(2)));
                        gpsDataBean.setYaw(Float.parseFloat(yaw));
                    } else {
                        gpsDataBean.setYaw(0f);
                    }
                }
            }
        }

        return gpsDataBean;

    }


    @Override
    public DataBean dataAnalysis(DataBean dataBean) {
        //        SerialPortData serialPortData = (SerialPortData) dataBean;
//        String gpsData = serialPortData.getRevStrData();
        return null;
    }

    @Override
    public DataBean dataAnalysis(byte[] data) {
        return null;
    }

}
