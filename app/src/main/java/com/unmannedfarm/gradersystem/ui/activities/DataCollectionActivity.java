package com.unmannedfarm.gradersystem.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.databinding.ActivityDataCollectionBinding;
import com.unmannedfarm.gradersystem.databinding.ActivityParamSettingBinding;
import com.unmannedfarm.gradersystem.ui.activityrelations.DataCollectionRelations;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseActivity;

public class DataCollectionActivity extends BaseActivity {

    public ActivityDataCollectionBinding dataCollectionBinding;

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_data_collection;
    }

    @Override
    protected void onCreateViewModule() {
        dataCollectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_collection);
    }

    @Override
    protected void newExternalRelations() {
        new DataCollectionRelations(this);
    }


    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}