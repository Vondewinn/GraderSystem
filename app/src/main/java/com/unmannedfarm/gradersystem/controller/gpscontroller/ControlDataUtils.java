package com.unmannedfarm.gradersystem.controller.gpscontroller;

import java.math.BigInteger;

public class ControlDataUtils {

    public static byte[] CANBridge(int pwm1Duty, int pwm2Duty, int pwm3Duty) {
        int a = 0 , b = 0;
        int c = 0 , d = 0;
        int e = 0 , f = 0;

        byte[] data = new byte[8];

        if(pwm1Duty != 0){
            if(pwm1Duty > 0){
                String string1 = intToHex(pwm1Duty);
                String str1 = string1.substring(0, 1);
                String str2 = string1.substring(1, 3);
                a = hex16to10(str1);
                b = hex16to10(str2);
            }else {
                String string2 = Integer.toHexString(pwm1Duty);  // hex = FFFF FC18
                String str3 = string2.substring(4, 6);
                String str4 = string2.substring(6, 8);
                a = hex16to10(str3);  // FC
                b = hex16to10(str4);  // 18
            }
        }

        if(pwm2Duty != 0){
            if(pwm2Duty > 0){
                String string3 = intToHex(pwm2Duty);
                String str5 = string3.substring(0, 1);
                String str6 = string3.substring(1, 3);
                c = hex16to10(str5);
                d = hex16to10(str6);
            }else {
                String string4 = Integer.toHexString(pwm2Duty);  // hex = FFFF FC18
                String str7 = string4.substring(4, 6);
                String str8 = string4.substring(6, 8);
                c = hex16to10(str7);  // FC
                d = hex16to10(str8);  // 18
            }
        }

        if(pwm3Duty != 0){
            if(pwm3Duty > 0){  //正数十进制转十六进制
                String string5 = intToHex(pwm3Duty);
                String str9 = string5.substring(0, 1);
                String str10 = string5.substring(1, 3);
                e = hex16to10(str9);
                f = hex16to10(str10);
            }else {             //负数十进制转十六进制
                String hex = Integer.toHexString(pwm3Duty);  // hex = FFFF FC18
                String str11 = hex.substring(4, 6);
                String str12 = hex.substring(6, 8);
                e = hex16to10(str11);  // FC
                f = hex16to10(str12);  // 18
            }
        }

        data[0] = (byte) b;
        data[1] = (byte) a;
        data[2] = (byte) d;
        data[3] = (byte) c;
        data[4] = (byte) f;
        data[5] = (byte) e;
        data[6] = 0x00;
        data[7] = 0x00;

        return data;

    }

    //byte转16进制字符串
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            // if (n<b.length-1) hs=hs+":";
        }
        return hs.toUpperCase();
    }

    //将十进制数转化为十六进制
    public static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }

    //十六进制转换成十进制
    public static int hex16to10(String strHex) {
        BigInteger IntNum = new BigInteger(strHex, 16);
        return IntNum.intValue();
    }

}
