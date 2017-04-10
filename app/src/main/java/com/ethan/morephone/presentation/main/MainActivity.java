package com.ethan.morephone.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFragment;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersActivity;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.utils.ActivityUtils;
import com.ethan.morephone.widget.NavigationTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/4/17.
 */

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private final int REQUEST_INCOMING_PHONE = 100;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(mToolbar, MyPreference.getPhoneNumber(getApplicationContext()));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigation();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof IncomingPhoneNumbersFragment) return;
        DashboardFragment numbersFragment = DashboardFragment.getInstance(MyPreference.getPhoneNumber(getApplicationContext()));
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                numbersFragment,
                R.id.content_frame,
                IncomingPhoneNumbersFragment.class.getSimpleName());
    }

    private void setUpViewPager() {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.tab_strip);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(viewPager, 0);
    }

    private void setUpNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_numbers:
                startActivityForResult(new Intent(this, IncomingPhoneNumbersActivity.class), REQUEST_INCOMING_PHONE);
                break;
            case R.id.nav_buy_number:
                startActivity(new Intent(this, SearchPhoneNumberActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_conversation, menu);
//
//        final MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(item,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
////                        mConversationListAdapter.setFilter(mConversationEntities);
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<MessageItem> filter(List<MessageItem> models, String query) {
        query = query.toLowerCase();
        final List<MessageItem> filteredModelList = new ArrayList<>();
        for (MessageItem model : models) {
            final String text = model.body.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_compose:
                startActivity(new Intent(this, ComposeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_INCOMING_PHONE && resultCode == RESULT_OK){
            DashboardFragment numbersFragment = DashboardFragment.getInstance(MyPreference.getPhoneNumber(getApplicationContext()));
            ActivityUtils.replaceFragmentToActivity(
                    getSupportFragmentManager(),
                    numbersFragment,
                    R.id.content_frame,
                    IncomingPhoneNumbersFragment.class.getSimpleName());
        }
    }
}
