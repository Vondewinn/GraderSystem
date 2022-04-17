package com.unmannedfarm.gradersystem.ui.activityrelations;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.manager.preferencemanager.PreferenceManager;
import com.unmannedfarm.gradersystem.ui.activities.ParamSettingActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.ActivityLifecycleListener;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseExternalRelations;

public class ParamSettingRelations extends BaseExternalRelations<ParamSettingActivity> {

    public ParamSettingRelations(ParamSettingActivity activity) {
        super(activity);



    }

    @Override
    protected ActivityLifecycleListener newActivityLifecycleListener() {
        return new ActivityLifecycleListener() {
            @Override
            public void onModulesCreated() {
                super.onModulesCreated();
                mActivity.activityParamSettingBinding.baseTitle.settingTitle.setText("参数设置");
                mActivity.activityParamSettingBinding.baseTitle.settingBackImg.setOnClickListener((v) -> mActivity.finish());
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

    private void initPara() {
        String HD = PreferenceManager.getStringValue(mActivity, "heightDead");
        String LD = PreferenceManager.getStringValue(mActivity, "levelDead");
        String SL = PreferenceManager.getStringValue(mActivity, "speedLevel");
        String width = PreferenceManager.getStringValue(mActivity, "machineWidth");
        boolean rb = PreferenceManager.getBooleanValue(mActivity, "rbDry");
        if (!HD.isEmpty()) {
            mActivity.activityParamSettingBinding.etSetControlHeight.setText(HD);
        }
        if (!LD.isEmpty()) {
            mActivity.activityParamSettingBinding.etSetControlLevel.setText(LD);
        }
        if (!SL.isEmpty()) {
            mActivity.activityParamSettingBinding.etSetControlSpeed.setText(SL);
        }
        if (!width.isEmpty()) {
            mActivity.activityParamSettingBinding.etSetWidth.setText(width);
        }
        if (rb) {
            mActivity.activityParamSettingBinding.rbDry.setChecked(true);
        } else {
            mActivity.activityParamSettingBinding.rbField.setChecked(true);
        }

    }

    private void setOnClickListener() {
        mActivity.activityParamSettingBinding.btnSettingOk.setOnClickListener((v) -> {
            String HD = mActivity.activityParamSettingBinding.etSetControlHeight.getText().toString();
            String LD = mActivity.activityParamSettingBinding.etSetControlLevel.getText().toString();
            String SL = mActivity.activityParamSettingBinding.etSetControlSpeed.getText().toString();
            String width = mActivity.activityParamSettingBinding.etSetWidth.getText().toString();
            if (!HD.isEmpty()) {
                PreferenceManager.saveStringValue(mActivity, HD, "heightDead");
            } else {
                mActivity.showToast("高程死区值不能为空");
                return;
            }
            if (!LD.isEmpty()) {
                PreferenceManager.saveStringValue(mActivity, LD, "levelDead");
            } else {
                mActivity.showToast("调平死区值不能为空");
                return;
            }
            if (!SL.isEmpty()) {
                PreferenceManager.saveStringValue(mActivity, SL, "speedLevel");
            } else {
                mActivity.showToast("控制速度等级不能为空");
                return;
            }
            if (!width.isEmpty()) {
                PreferenceManager.saveStringValue(mActivity, width, "machineWidth");
            } else {
                mActivity.showToast("机具幅宽不能为空");
                return;
            }
            mActivity.finish();
        });

        mActivity.activityParamSettingBinding.rgWorkMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                if (group.getId() == mActivity.activityParamSettingBinding.rgWorkMode.getId()) {
                    if (id == mActivity.activityParamSettingBinding.rbDry.getId()) {
                        PreferenceManager.saveBooleanValue(mActivity, true, "rbDry");
                    } else if(id == mActivity.activityParamSettingBinding.rbField.getId()) {
                        PreferenceManager.saveBooleanValue(mActivity, false, "rbDry");
                    }
                }
            }
        });

        mActivity.activityParamSettingBinding.btnSettingCancle.setOnClickListener((v) -> mActivity.finish());

    }



}
