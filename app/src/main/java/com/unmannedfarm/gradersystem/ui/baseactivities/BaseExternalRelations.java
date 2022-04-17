package com.unmannedfarm.gradersystem.ui.baseactivities;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件动机模式的外部关系模块
 * 备注：类BaseExternalRelations<Activity extends BaseActivity>作为所有外部关系模块的顶层父类
 */
public class BaseExternalRelations<Activity extends BaseActivity> { //该传入的Activity泛型必须是BaseActivity子类，或者是BaseActivity

    protected Activity mActivity;

    public BaseExternalRelations(Activity activity) {
        mActivity = activity;
        mActivity.setLifecycleListener(newActivityLifecycleListener());
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected Boolean isRegisterEventBus() {
        return false;
    }

    protected ActivityLifecycleListener newActivityLifecycleListener() {
        return new ActivityLifecycleListener(){
        };
    }
}
