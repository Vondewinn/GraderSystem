package com.unmannedfarm.gradersystem.utils;

public class LocationUtils {

    /**
     * @fun    大地坐标转东北天坐标
     * @author Vondewinn
     * @date   2022-3-28
     * */
    public static double[] blhToXyz(double B, double L, double H){
        double a = 6378137.0; //84坐标系
        double e = Math.sqrt(0.00669437999013);
        double scale_wide = 3;
        double[] result = new double[3];
        double A_ = 1
                +3*e*e/4
                +45*e*e*e*e/64
                +175*e*e*e*e*e*e/256
                +11025*e*e*e*e*e*e*e*e/16384
                +43659*e*e*e*e*e*e*e*e*e*e/65536;
        double B_ = 3*e*e/4+15*e*e*e*e/16
                +525*e*e*e*e*e*e/512
                +2205*e*e*e*e*e*e*e*e/2048
                +72765*e*e*e*e*e*e*e*e*e*e/65536;
        double C_ =  15*e*e*e*e/64
                +105*e*e*e*e*e*e/256
                +2205*e*e*e*e*e*e*e*e/4096
                +10395*e*e*e*e*e*e*e*e*e*e/16384;
        double D_ =  35*e*e*e*e*e*e/512
                +315*e*e*e*e*e*e*e*e/2048
                +31185*e*e*e*e*e*e*e*e*e*e/13072;
        double α  =  A_*a*(1-e*e);
        double β  = -B_*a*(1-e*e)/2;
        double γ  =  C_*a*(1-e*e)/4;
        double δ  = -D_*a*(1-e*e)/6;
        double C0 =  α;
        double C1 =  2*β+4*γ+6*δ;
        double C2 = -8*γ-32*δ;
        double C3 =  32*δ;
        double x,y,sign;
        double scale_number = Math.floor(L/scale_wide);
        if(L > (scale_number * scale_wide + scale_wide/2)){
            scale_number =scale_number + 1;
            sign = -1;
        }else{
            sign =  1;
        }
        double L0 =  scale_wide*scale_number;
        double l  =  Math.abs(L-L0);
        B = B * Math.PI /180;
        l = l * Math.PI /180;
        double t  =  Math.tan(B);
        double m0 =  Math.cos(B)*l;
        double η  =  Math.sqrt(e*e*Math.pow(Math.cos(B),2)/(1-e*e));
        double N  =  a/Math.sqrt(1-e*e*Math.pow(Math.sin(B), 2));
        double X0 =  C0*B+Math.cos(B)*(C1*Math.sin(B)+C2*Math.pow(Math.sin(B),3)+C3*Math.pow(Math.sin(B), 5));
        x  =  X0
                +N*t*m0*m0/2
                +N*t*m0*m0*m0*m0*(5-t*t+9*η*η+4*η*η*η*η)/24
                +N*t*m0*m0*m0*m0*m0*m0*(61-58*t*t+t*t*t*t)/720;
        y  =  N*m0
                +N*m0*m0*m0*(1-t*t+η*η)/6
                +N*m0*m0*m0*m0*m0*m0*(5-18*t*t+t*t*t*t+14*η*η-58*η*η*t*t)/120;
        y =   y*sign + 500000;
        double h = H;

        result[0] = x;
        result[1] = y;
        result[2] = h;
        return result;
    }

}
