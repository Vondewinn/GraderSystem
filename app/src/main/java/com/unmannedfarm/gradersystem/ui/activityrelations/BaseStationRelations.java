package com.unmannedfarm.gradersystem.ui.activityrelations;

import android.view.View;
import android.widget.Toast;

import com.unmannedfarm.gradersystem.bean.avtivitybean.BaseStationBean;
import com.unmannedfarm.gradersystem.manager.preferencemanager.PreferenceManager;
import com.unmannedfarm.gradersystem.ui.activities.BaseStationActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseActivity;
import com.unmannedfarm.gradersystem.ui.baseactivities.BaseExternalRelations;

import org.greenrobot.eventbus.EventBus;

public class BaseStationRelations extends BaseExternalRelations<BaseStationActivity> {

    private BaseStationBean baseStationBean = new BaseStationBean();

    public BaseStationRelations(BaseStationActivity activity) {
        super(activity);

        mActivity.setBtnSaveListener(btnSaveClickListener());
        initPara();
    }

    private View.OnClickListener btnSaveClickListener(){
        return (v) -> {

            String ip = mActivity.getEtIp();
            if (!ip.isEmpty()){
                PreferenceManager.saveStringValue(mActivity, ip, "ip");
            } else {
                Toast.makeText(mActivity, "请输入域名/IP", Toast.LENGTH_SHORT).show();
                return;
            }
            String port = mActivity.getEtPort();
            if (!port.isEmpty()){
                PreferenceManager.saveStringValue(mActivity, port, "port");
            } else {
                Toast.makeText(mActivity, "请输入端口值", Toast.LENGTH_SHORT).show();
                return;
            }
            String account = mActivity.getEtAccount();
            if (!account.isEmpty()){
                PreferenceManager.saveStringValue(mActivity, account, "account");
            } else {
                Toast.makeText(mActivity, "请输入差分账号", Toast.LENGTH_SHORT).show();
                return;
            }
            String pwd = mActivity.getEtPassword();
            if (!pwd.isEmpty()){
                PreferenceManager.saveStringValue(mActivity, pwd, "pwd");
            } else {
                Toast.makeText(mActivity, "请输入差分密码", Toast.LENGTH_SHORT).show();
                return;
            }
            String mtp = mActivity.getEtMountPoint();
            if (!mtp.isEmpty()){
                PreferenceManager.saveStringValue(mActivity, mtp, "mtp");
            } else {
                Toast.makeText(mActivity, "请输入挂载点", Toast.LENGTH_SHORT).show();
                return;
            }

            baseStationBean.setIp(ip);
            baseStationBean.setPort(port);
            baseStationBean.setAccount(account);
            baseStationBean.setPassword(pwd);
            baseStationBean.setMountedPoint(mtp);
            EventBus.getDefault().post(baseStationBean);

            mActivity.finish();

        };
    }

    private void initPara(){
        mActivity.setEtIp(PreferenceManager.getStringValue(mActivity, "ip"));
        mActivity.setEtPort(PreferenceManager.getStringValue(mActivity, "port"));
        mActivity.setEtAccount(PreferenceManager.getStringValue(mActivity, "account"));
        mActivity.setEtPassword(PreferenceManager.getStringValue(mActivity, "pwd"));
        mActivity.setEtMountPoint(PreferenceManager.getStringValue(mActivity, "mtp"));
    }


}
