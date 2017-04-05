package com.wulei.runner.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.fragment.FragmentMap;
import com.wulei.runner.fragment.FragmentNews;
import com.wulei.runner.fragment.FragmentRun;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int TAG = 1;
    //flag,退出
    private boolean isQuit = false;
    //toolbar
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //抽屉
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    //抽屉view
    @BindView(R.id.navigation)
    public NavigationView mNavigationView;
    //运动的fragment
    private FragmentRun mFragmentRun = (FragmentRun) FragmentUtils.newInstance(ConstantFactory.TAG_RUN);
    private FragmentMap mFragmentMap = (FragmentMap) FragmentUtils.newInstance(ConstantFactory.TAG_MAP);
    private FragmentNews mFragmentNews = (FragmentNews) FragmentUtils.newInstance(ConstantFactory.TAG_NEWS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //导航栏监听
        mNavigationView.setNavigationItemSelectedListener(this);

        //toolbar设置
        setSupportActionBar(mToolbar);
        //添加navigation icon
        ActionBarDrawerToggle abt = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.toolbar_drawer_open, R.string.toolbar_drawer_close);
        mDrawerLayout.addDrawerListener(abt);
        abt.syncState();

        //toolbar监听器
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    /**
     * activity重启异常，fragment重叠，处理hide，show
     */
    @Override
    protected void hideShowFragment() {
        FragmentRun fragmentRun = null;
        FragmentMap fragmentMap = null;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof FragmentRun) {
                fragmentRun = (FragmentRun) fragment;
            } else if (fragment instanceof FragmentMap) {
                fragmentMap = (FragmentMap) fragment;
            }
            // 解决重叠问题
            getSupportFragmentManager().beginTransaction()
                    .show(fragmentRun)
                    .hide(fragmentMap)
                    .commit();


        }
    }

    /**
     * activity默认初始化的fragment
     *
     * @return
     */
    @Override
    public String getFragmentTag() {
        return ConstantFactory.TAG_RUN;
    }

    /**
     * activity默认加载根view
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * 抽屉的点击事件，fragment的切换
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.run:

                //显示
                FragmentUtils.show(this, mFragmentRun);
                FragmentUtils.hide(this, mFragmentMap);
                FragmentUtils.replace(this,mFragmentRun,ConstantFactory.TAG_RUN,false);
                mNavigationView.setCheckedItem(R.id.run);
                break;
            case R.id.goal:
                mNavigationView.setCheckedItem(R.id.goal);
                break;
            case R.id.record:
                mNavigationView.setCheckedItem(R.id.record);
                break;
            case R.id.news:
                FragmentUtils.replace(this, mFragmentNews, ConstantFactory.TAG_NEWS,true);
                mNavigationView.setCheckedItem(R.id.news);
                break;
            case R.id.rank:
                mNavigationView.setCheckedItem(R.id.rank);
                break;
            case R.id.setting:
                break;
            case R.id.remind:
                break;
            case R.id.shard:
                break;
            case R.id.timing:
                break;
        }
        //关闭抽屉
        mDrawerLayout.closeDrawers();
        return false;
    }


    /**
     * handler处理退出的消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TAG) {
                isQuit = false;
            }
        }
    };

    /**
     * 重写返回按钮，设置为再按一次退出程序
     */
    @Override
    public void onBackPressed() {
        /*
         * 如果抽屉打开就关闭，然后返回，不执行下面的语句
         */
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            //抽屉打开时，按下返回键关闭
            mDrawerLayout.closeDrawers();
            return;
        }

        /*
         * 如果抽屉关闭，则执行确认操作。
         */
        if (isQuit && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            finish();
        } else {
            mHandler.sendEmptyMessageDelayed(TAG, 2000);
            ToastUtil.show(R.string.toast_quit);
            isQuit = true;
        }

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:

                break;
        }
    }
}
