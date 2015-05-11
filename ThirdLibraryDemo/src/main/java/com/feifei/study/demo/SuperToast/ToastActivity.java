package com.feifei.study.demo.SuperToast;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.feifei.study.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ToastActivity extends Activity {

    private static final String NAVIGAION_SELECTION = "navigationSelection";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_toast);
        //这是一个actionbar的导航
        final ActionBar actionBar = getActionBar ();
        actionBar.setNavigationMode (ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (
                actionBar.getThemedContext (), android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, getResources ().getStringArray (
                R.array.navigation_list));
        arrayAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks (arrayAdapter,
                new ActionBar.OnNavigationListener () {
                    Fragment fragment;

                    @Override
                    public boolean onNavigationItemSelected (int itemPosition,
                                                             long itemId) {
                        switch (itemPosition) {
                            case 0:
                            case 1:
                            case 2:
                                fragment = new FragmentSuperCardToast ();
                                break;
                        }
                        FragmentTransaction fragmentTransaction = getFragmentManager ()
                                .beginTransaction ();
                        fragmentTransaction.replace (R.id.fragment_container,
                                fragment);
                        fragmentTransaction.commit ();
                        return false;
                    }
                });

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem (savedInstanceState
                    .getInt (NAVIGAION_SELECTION));
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate (R.menu.main, menu);
        return true;

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        //导航的状态的保存
        outState.putInt (NAVIGAION_SELECTION, getActionBar ()
                .getSelectedNavigationIndex ());

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            //这里再判断是在哪一个fragment
            case R.id.wiki:
                switch (getActionBar ().getSelectedNavigationIndex ()) {
                    case 0:
                        gotoWebsite (getResources ().getString (
                                R.string.url_wiki_supertoast));
                        break;
                    case 1:
                        gotoWebsite (getResources ().getString (
                                R.string.url_wiki_superactivitytoast));
                        break;
                    case 2:
                        gotoWebsite (getResources ().getString (
                                R.string.url_wiki_supercardtoast));
                        break;
                }
                return true;
            case R.id.github:
                gotoWebsite (getResources ().getString (R.string.url_project_page));
                return true;
            default:
                return super.onOptionsItemSelected (item);

        }

    }

    private void gotoWebsite (String url) {
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData (Uri.parse (url));
        startActivity (intent);
    }
}

