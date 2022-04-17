package com.unmannedfarm.gradersystem.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.databinding.ActivityParamSettingBinding;
import com.unmannedfarm.gradersystem.ui.activityrelations.ParamSettingRelations;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseActivity;

public class ParamSettingActivity extends BaseActivity {

    public ActivityParamSettingBinding activityParamSettingBinding;

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_param_setting;
    }

    @Override
    protected void onCreateViewModule() {
        activityParamSettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_param_setting);
    }

    @Override
    protected void newExternalRelations() {
        new ParamSettingRelations(this);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}