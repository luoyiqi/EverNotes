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

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.feifei.study.R;

import java.util.Arrays;
import java.util.LinkedList;

import feifei.library.refreshview.base.SoundPullEventListener;
import feifei.library.refreshview.view.RefreshBase;
import feifei.library.refreshview.view.RefreshListView;


/**
 * 作者    裴智飞
 * 时间    15-5-10 上午12:02
 * 文件    Study
 * 描述
 * getRefreshableView()获取里面的view，诸如weibview，scrollview，他是进行了一次封装，比如要对listview的item设置监听就要先取出来
 * mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {
 * public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {//下拉刷新}
 * public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {//上拉分页}
 * });
 * <p/>
 * mPullRefreshGridView.setEmptyView(tv);
 * mGridView.setAdapter(mAdapter);
 * setMode(mPullRefreshGridView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START:Mode.BOTH);
 * 数据完成之后停止刷新，调用mPullRefreshGridView.onRefreshComplete();
 */
public final class RefreshListActivity extends ListActivity {

    static final int MENU_MANUAL_REFRESH = 0;
    static final int MENU_DISABLE_SCROLL = 1;
    static final int MENU_SET_MODE = 2;
    static final int MENU_DEMO = 3;

    private LinkedList<String> mListItems;
    private RefreshListView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_listview);

        mPullRefreshListView = (RefreshListView) findViewById (R.id.pull_refresh_list);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener (new RefreshBase.OnRefreshListener<ListView> () {
            @Override
            public void onRefresh (RefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime (getApplicationContext (), System.currentTimeMillis (),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy ().setLastUpdatedLabel (label);
                // Do work to refresh the list here.
                new GetDataTask ().execute ();
            }
        });

        // 相当于自动刷新的功能
        mPullRefreshListView.setOnLastItemVisibleListener (new RefreshBase.OnLastItemVisibleListener () {
            @Override
            public void onLastItemVisible () {
                Toast.makeText (RefreshListActivity.this, "End of List!", Toast.LENGTH_SHORT).show ();
            }
        });

        ListView actualListView = mPullRefreshListView.getRefreshableView ();
        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu (actualListView);
        mListItems = new LinkedList<String> ();
        mListItems.addAll (Arrays.asList (mStrings));
        mAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, mListItems);

        //添加声音监听器
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView> (this);
        // soundListener.addSoundEvent (RefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        // soundListener.addSoundEvent (RefreshBase.State.RESET, R.raw.reset_sound);
        // soundListener.addSoundEvent (RefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        mPullRefreshListView.setOnPullEventListener ((RefreshBase.OnPullEventListener<ListView>) soundListener);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        actualListView.setAdapter (mAdapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground (Void... params) {
            try {
                Thread.sleep (1000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute (String[] result) {
            mListItems.addFirst ("Added after refresh...");
            mAdapter.notifyDataSetChanged ();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete ();
            super.onPostExecute (result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        menu.add (0, MENU_MANUAL_REFRESH, 0, "正常刷新");
        menu.add (0, MENU_DISABLE_SCROLL, 1,
                mPullRefreshListView.isScrollingWhileRefreshingEnabled () ? "滑动时不可刷新"
                        : "滑动可刷新");
        menu.add (0, MENU_SET_MODE, 0, mPullRefreshListView.getMode () == RefreshBase.Mode.BOTH ? "向下滑"
                : "上下都可滑");
        menu.add (0, MENU_DEMO, 0, "演示");
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle ("Item: " + getListView ().getItemAtPosition (info.position));
        menu.add ("Item 1");
        menu.add ("Item 2");
        menu.add ("Item 3");
        menu.add ("Item 4");
        super.onCreateContextMenu (menu, v, menuInfo);
    }

    //改变菜单文字
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem disableItem = menu.findItem (MENU_DISABLE_SCROLL);
        disableItem
                .setTitle (mPullRefreshListView.isScrollingWhileRefreshingEnabled () ? "滑动时不可刷新"
                        : "滑动可刷新");
        MenuItem setModeItem = menu.findItem (MENU_SET_MODE);
        setModeItem.setTitle (mPullRefreshListView.getMode () == RefreshBase.Mode.BOTH ? "向下滑"
                : "上下都可滑H");

        return super.onPrepareOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        switch (item.getItemId ()) {
            case MENU_MANUAL_REFRESH:
                new GetDataTask ().execute ();
                mPullRefreshListView.setRefreshing (false);
                break;
            case MENU_DISABLE_SCROLL:
                mPullRefreshListView.setScrollingWhileRefreshingEnabled (!mPullRefreshListView
                        .isScrollingWhileRefreshingEnabled ());
                break;
            case MENU_SET_MODE:
                mPullRefreshListView.setMode (mPullRefreshListView.getMode () == RefreshBase.Mode.BOTH ? RefreshBase.Mode.PULL_FROM_START
                        : RefreshBase.Mode.BOTH);
                break;
            case MENU_DEMO:
                mPullRefreshListView.demo ();
                break;
        }

        return super.onOptionsItemSelected (item);
    }

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
}
