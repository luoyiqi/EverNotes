package com.feifei.study.demo.RefreshView;


import android.app.ExpandableListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.feifei.study.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feifei.library.refreshview.view.RefreshBase;
import feifei.library.refreshview.view.RefreshExpandableListView;


public final class RefreshDemo extends ExpandableListActivity {
    private static final String KEY = "key";

    private List<Map<String, String>> groupData = new ArrayList<Map<String, String>> ();
    private List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>> ();

    private RefreshExpandableListView mPullRefreshListView;
    private SimpleExpandableListAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_refresh);
        mPullRefreshListView = (RefreshExpandableListView) findViewById (R.id.pull_refresh_list);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener (new RefreshBase.OnRefreshListener2<ExpandableListView> () {
            @Override
            public void onPullDownToRefresh (RefreshBase<ExpandableListView> refreshView) {
                new GetDataTask ().execute ();
            }

            @Override
            public void onPullUpToRefresh (RefreshBase<ExpandableListView> refreshView) {
                new GetDataTask ().execute ();
            }
        });

        for (String group : mGroupStrings) {
            Map<String, String> groupMap1 = new HashMap<String, String> ();
            groupData.add (groupMap1);
            groupMap1.put (KEY, group);

            List<Map<String, String>> childList = new ArrayList<Map<String, String>> ();
            for (String string : mChildStrings) {
                Map<String, String> childMap = new HashMap<String, String> ();
                childList.add (childMap);
                childMap.put (KEY, string);
            }
            childData.add (childList);
        }

        mAdapter = new SimpleExpandableListAdapter (this, groupData, android.R.layout.simple_expandable_list_item_1,
                new String[]{KEY}, new int[]{android.R.id.text1}, childData,
                android.R.layout.simple_expandable_list_item_2, new String[]{KEY}, new int[]{android.R.id.text1});
        setListAdapter (mAdapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground (Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep (1000);
            } catch (InterruptedException e) {
            }
            return mChildStrings;
        }

        @Override
        protected void onPostExecute (String[] result) {
            Map<String, String> newMap = new HashMap<String, String> ();
            newMap.put (KEY, "Added after refresh...");
            groupData.add (newMap);

            List<Map<String, String>> childList = new ArrayList<Map<String, String>> ();
            for (String string : mChildStrings) {
                Map<String, String> childMap = new HashMap<String, String> ();
                childMap.put (KEY, string);
                childList.add (childMap);
            }
            childData.add (childList);

            mAdapter.notifyDataSetChanged ();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete ();

            super.onPostExecute (result);
        }
    }

    private String[] mChildStrings = {"Child One", "Child Two", "Child Three", "Child Four", "Child Five", "Child Six"};

    private String[] mGroupStrings = {"Group One", "Group Two", "Group Three"};
}

