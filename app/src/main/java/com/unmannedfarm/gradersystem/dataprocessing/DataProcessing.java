package com.unmannedfarm.gradersystem.dataprocessing;

public interface DataProcessing {

    DataBean dataAnalysis(DataBean dataBean);

    DataBean dataAnalysis(byte[] data);

    DataBean dataAnalysis(String data);

}
