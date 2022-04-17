package com.unmannedfarm.gradersystem.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.unmannedfarm.gradersystem.R;
import com.unmannedfarm.gradersystem.ui.activityrelations.BaseStationRelations;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseExternalRelations;

public class BaseStationActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView btnBack;
    private Button btnSave, btnCancel;

    private RadioGroup rgMode;
    private RadioButton rbNetwork, rbRadio, rbStar;
    private EditText etIp, etPort, etAccount, etPassword, etMountPoint;


    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_base_station;
    }

    @Override
    protected void onCreateViewModule() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //默认不显示软键盘
        findViewById();
        init();
        setInnerOnclickListener();

    }

    @Override
    protected void newExternalRelations() {
        new BaseStationRelations(this);
    }

    private void findViewById() {
        tvTitle = findViewById(R.id.setting_title);
        btnBack = findViewById(R.id.setting_back_img);
        btnSave = findViewById(R.id.btn_bs_save);
        btnCancel = findViewById(R.id.btn_bs_cancel);
        rgMode = findViewById(R.id.rg_modes);
        rbNetwork= findViewById(R.id.rb_network);
        rbRadio = findViewById(R.id.rb_radio);
        rbStar = findViewById(R.id.rb_star);
        etIp = findViewById(R.id.et_ip);
        etPort = findViewById(R.id.et_port);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_pwd);
        etMountPoint = findViewById(R.id.et_mpt);
    }

    private void init() {
        tvTitle.setText("差分基站配置");
    }

    private void setInnerOnclickListener() {
        btnBack.setOnClickListener((v) -> {
            finish();
        });
        btnCancel.setOnClickListener((v) -> finish());
        rgMode.setOnCheckedChangeListener(new radioGroupListener());    //设置按键监听
    }

    public void setBtnSaveListener(View.OnClickListener btnSaveOnClickListener){ //设置保存按键监听器接口
        btnSave.setOnClickListener(btnSaveOnClickListener);
    }

    public void setEtIp(String ip) {
        etIp.setText(ip);
    }

    public String getEtIp() {
        return etIp.getText().toString();
    }

    public String getEtPort() {
        return etPort.getText().toString();
    }

    public void setEtPort(String etPort) {
        this.etPort.setText(etPort);
    }

    public String getEtAccount() {
        return etAccount.getText().toString();
    }

    public void setEtAccount(String etAccount) {
        this.etAccount.setText(etAccount);
    }

    public String getEtPassword() {
        return etPassword.getText().toString();
    }

    public void setEtPassword(String etPassword) {
        this.etPassword.setText(etPassword);
    }

    public String getEtMountPoint() {
        return etMountPoint.getText().toString();
    }

    public void setEtMountPoint(String etMountPoint) {
        this.etMountPoint.setText(etMountPoint);
    }

    /**
     * 按键选择内部类
     * */
    class radioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(group.getId() == rgMode.getId()){
                if(checkedId == R.id.rb_network){
                     //切换到网络差分模式
                }
                else if(checkedId == R.id.rb_radio){
                     //切换到电台模式
                }
                else if(checkedId == R.id.rb_star){
                    //切换到星基模式
                }
            }
        }
    }



}