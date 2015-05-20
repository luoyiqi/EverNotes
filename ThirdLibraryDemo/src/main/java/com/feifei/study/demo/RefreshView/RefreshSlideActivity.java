/**
 * ****************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.feifei.study.demo.RefreshView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.feifei.study.R;

import java.util.Arrays;
import java.util.LinkedList;

import feifei.library.refreshview.base.SlideCutListView;
import feifei.library.refreshview.view.RefreshBase;
import feifei.library.refreshview.view.RefreshSlideCutListView;

public final class RefreshSlideActivity extends Activity implements SlideCutListView.RemoveListener {

    static final int MENU_SET_MODE = 0;

    private LinkedList<String> mListItems;
    private RefreshSlideCutListView mPullRefreshSlideView;
    private SlideCutListView mSlideView;
    private ArrayAdapter<String> mAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_swipeview);

        mPullRefreshSlideView = (RefreshSlideCutListView) findViewById (R.id.pull_refresh_grid);
        mSlideView = mPullRefreshSlideView.getRefreshableView ();
        mSlideView.setRemoveListener (this);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshSlideView.setOnRefreshListener (new RefreshBase.OnRefreshListener2<SlideCutListView> () {

            @Override
            public void onPullDownToRefresh (RefreshBase<SlideCutListView> refreshView) {
                Toast.makeText (RefreshSlideActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show ();
                new GetDataTask ().execute ();
            }

            @Override
            public void onPullUpToRefresh (RefreshBase<SlideCutListView> refreshView) {
                Toast.makeText (RefreshSlideActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show ();
                new GetDataTask ().execute ();
            }

        });

        mListItems = new LinkedList<String> ();

        TextView tv = new TextView (this);
        tv.setGravity (Gravity.CENTER);
        tv.setText ("Empty View, Pull Down/Up to Add Items");
        mPullRefreshSlideView.setEmptyView (tv);

        mAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, mListItems);
        mSlideView.setAdapter (mAdapter);
    }
    //pullToRefresh有个问题就是小标差1，要list.get(position-1)
    @Override
    public void removeItem (SlideCutListView.RemoveDirection direction, int position) {
        switch (direction) {
            case RIGHT:
                Toast.makeText (this, "向右滑", Toast.LENGTH_SHORT).show ();
                break;
            case LEFT:
                Toast.makeText (this, "向左滑", Toast.LENGTH_SHORT).show ();
                break;
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground (Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep (2000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute (String[] result) {
            mListItems.addFirst ("Added after refresh...");
            mListItems.addAll (Arrays.asList (result));
            mAdapter.notifyDataSetChanged ();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshSlideView.onRefreshComplete ();

            super.onPostExecute (result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        menu.add (0, MENU_SET_MODE, 0,
                mPullRefreshSlideView.getMode () == RefreshBase.Mode.BOTH ? "Change to MODE_PULL_DOWN"
                        : "Change to MODE_PULL_BOTH");
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem setModeItem = menu.findItem (MENU_SET_MODE);
        setModeItem.setTitle (mPullRefreshSlideView.getMode () == RefreshBase.Mode.BOTH ? "Change to MODE_PULL_FROM_START"
                : "Change to MODE_PULL_BOTH");

        return super.onPrepareOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case MENU_SET_MODE:
                mPullRefreshSlideView
                        .setMode (mPullRefreshSlideView.getMode () == RefreshBase.Mode.BOTH ? RefreshBase.Mode.PULL_FROM_START
                                : RefreshBase.Mode.BOTH);
                break;
        }

        return super.onOptionsItemSelected (item);
    }

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
}