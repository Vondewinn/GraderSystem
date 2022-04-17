package com.unmannedfarm.gradersystem.ui.activityrelations;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.unmannedfarm.gradersystem.bean.avtivitybean.DataCollectionBean;
import com.unmannedfarm.gradersystem.dataprocessing.DataProcessingBean;
import com.unmannedfarm.gradersystem.ui.activities.DataCollectionActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.ActivityLifecycleListener;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseExternalRelations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class DataCollectionRelations extends BaseExternalRelations<DataCollectionActivity> {

    private DecimalFormat df3 = new DecimalFormat("0.000");

    public DataCollectionRelations(DataCollectionActivity activity) {
        super(activity);


    }

    /**
     * @fun : 开启EventBus
     * */
    @Override
    protected Boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected ActivityLifecycleListener newActivityLifecycleListener() {
        return new ActivityLifecycleListener() {
            @Override
            public void onModulesCreated() {
                super.onModulesCreated();
                mActivity.dataCollectionBinding.dataCollectionTitle.settingTitle.setText("数据采集");
                mActivity.dataCollectionBinding.dataCollectionTitle.settingBackImg.setOnClickListener((v) -> mActivity.finish());
                setOnClickListener();
                initPara();
            }

            @Override
            public void onResume() {
                super.onResume();

            }

            @Override
            public void onDestroy() {
                super.onDestroy();

            }
        };
    }

    private void setOnClickListener() {
        mActivity.dataCollectionBinding.dcStartSave.setOnClickListener((v) -> {
            showNormalDialog(true);
        });
        mActivity.dataCollectionBinding.dcStopSave.setOnClickListener((v) -> {
            showNormalDialog(false);
        });
    }

    private void initPara() {

    }
    // 数据采集界面显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void revDataProcessingBean(DataProcessingBean dataProcessingBean) {
        mActivity.dataCollectionBinding.dcRoll01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getRoll()));
        mActivity.dataCollectionBinding.dcPitch01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getPitch()));
        mActivity.dataCollectionBinding.dcYaw01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getYaw()));
        mActivity.dataCollectionBinding.dcGyroX01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getGyroX()));
        mActivity.dataCollectionBinding.dcGyroY01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getGyroY()));
        mActivity.dataCollectionBinding.dcGyroZ01.setText(df3.format(dataProcessingBean.getAhrsDataBean01().getGyroZ()));
        mActivity.dataCollectionBinding.dcAx01.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAx()));
        mActivity.dataCollectionBinding.dcAy01.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAy()));
        mActivity.dataCollectionBinding.dcAz01.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAz()));
        mActivity.dataCollectionBinding.dcRoll02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getRoll()));
        mActivity.dataCollectionBinding.dcPitch02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getPitch()));
        mActivity.dataCollectionBinding.dcYaw02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getYaw()));
        mActivity.dataCollectionBinding.dcGyroX02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getGyroX()));
        mActivity.dataCollectionBinding.dcGyroY02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getGyroY()));
        mActivity.dataCollectionBinding.dcGyroZ02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getGyroZ()));
        mActivity.dataCollectionBinding.dcAx02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAx()));
        mActivity.dataCollectionBinding.dcAy02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAy()));
        mActivity.dataCollectionBinding.dcAz02.setText(df3.format(dataProcessingBean.getAhrsDataBean02().getAz()));
        mActivity.dataCollectionBinding.dcLat.setText(dataProcessingBean.getGpsDataBean().getGpsLat() + "");
        mActivity.dataCollectionBinding.dcLng.setText(dataProcessingBean.getGpsDataBean().getGpsLng() + "");
        mActivity.dataCollectionBinding.dcHeight.setText(df3.format(dataProcessingBean.getGpsDataBean().getHeightValue()));
        mActivity.dataCollectionBinding.dcGpsYaw.setText(df3.format(dataProcessingBean.getGpsDataBean().getYaw()));
        mActivity.dataCollectionBinding.dcGpsRtk.setText(dataProcessingBean.getGpsDataBean().getGpsRtkQuality() + "");
        mActivity.dataCollectionBinding.dcGpsTimeout.setText(dataProcessingBean.getGpsDataBean().getDiffTimeout() + "");
    }

    private void showNormalDialog(boolean startSaveFlag) {
        AlertDialog.Builder dialog = new AlertDialog.Builder (mActivity);
        if (startSaveFlag) dialog.setTitle ("提示").setMessage ("是否开始保存数据？");
        else dialog.setTitle ("提示").setMessage ("是否停止保存数据？");
        //点击确定开始保存数据
        dialog.setPositiveButton ("确定", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCollectionBean dataCollectionBean = new DataCollectionBean();
                if (startSaveFlag) {
                    dataCollectionBean.setStartSaveFlag(true);
                    mActivity.showToast("开始保存数据");
                } else {
                    dataCollectionBean.setStartSaveFlag(false);
                    mActivity.showToast("停止保存数据");
                }
                EventBus.getDefault().post(dataCollectionBean);
            }
        });
        //如果取消，就什么都不做，关闭对话框
        dialog.setNegativeButton ("取消",null);
        dialog.show ();
    }


}
















