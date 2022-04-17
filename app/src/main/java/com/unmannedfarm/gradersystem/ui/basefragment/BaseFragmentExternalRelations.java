package com.unmannedfarm.gradersystem.ui.basefragment;

/**
 * 事件动机模式的外部关系模块
 */
public class BaseFragmentExternalRelations<Fragment extends BaseFragment> {

    protected Fragment mFragment;

    public BaseFragmentExternalRelations(Fragment fragment) {
        mFragment = fragment;
        mFragment.setLifecycleListener(newFragmentLifecycleListener());
    }

    protected FragmentLifecycleListener newFragmentLifecycleListener() {
        return new FragmentLifecycleListener(){
        };
    }
}
