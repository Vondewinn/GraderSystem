package com.unmannedfarm.gradersystem.dataprocessing;

import com.unmannedfarm.gradersystem.dataacquisition.canbus.can.CanData;
import com.unmannedfarm.gradersystem.dataprocessing.ahrsdata.AhrsDataBean;
import com.unmannedfarm.gradersystem.dataprocessing.gpsdata.GpsDataBean;

public class DataProcessingBean {

    private AhrsDataBean ahrsDataBean01 = new AhrsDataBean();
    private AhrsDataBean ahrsDataBean02 = new AhrsDataBean();
    private AhrsDataBean ahrsDataBean03 = new AhrsDataBean();
    private AhrsDataBean ahrsDataBean04 = new AhrsDataBean();
    private GpsDataBean  gpsDataBean  = new GpsDataBean();
    private CanData      canData      = new CanData();

    public AhrsDataBean getAhrsDataBean01() {
        return ahrsDataBean01;
    }

    public void setAhrsDataBean01(AhrsDataBean ahrsDataBean01) {
        this.ahrsDataBean01 = ahrsDataBean01;
    }

    public AhrsDataBean getAhrsDataBean02() {
        return ahrsDataBean02;
    }

    public void setAhrsDataBean02(AhrsDataBean ahrsDataBean02) {
        this.ahrsDataBean02 = ahrsDataBean02;
    }

    public AhrsDataBean getAhrsDataBean03() {
        return ahrsDataBean03;
    }

    public void setAhrsDataBean03(AhrsDataBean ahrsDataBean03) {
        this.ahrsDataBean03 = ahrsDataBean03;
    }

    public AhrsDataBean getAhrsDataBean04() {
        return ahrsDataBean04;
    }

    public void setAhrsDataBean04(AhrsDataBean ahrsDataBean04) {
        this.ahrsDataBean04 = ahrsDataBean04;
    }

    public GpsDataBean getGpsDataBean() {
        return gpsDataBean;
    }

    public void setGpsDataBean(GpsDataBean gpsDataBean) {
        this.gpsDataBean = gpsDataBean;
    }

    public CanData getCanData() {
        return canData;
    }

    public void setCanData(CanData canData) {
        this.canData = canData;
    }

}
