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
package feifei.library.refreshview.view;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

abstract class RefreshBaseListFragment<T extends RefreshBase<? extends AbsListView>> extends ListFragment {

    private T refreshListView;

    @Override
    public final View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView (inflater, container, savedInstanceState);

        ListView lv = (ListView) layout.findViewById (android.R.id.list);
        ViewGroup parent = (ViewGroup) lv.getParent ();

        // Remove ListView and add RefreshListView in its place
        int lvIndex = parent.indexOfChild (lv);
        parent.removeViewAt (lvIndex);
        refreshListView = onCreatePullToRefreshListView (inflater, savedInstanceState);
        parent.addView (refreshListView, lvIndex, lv.getLayoutParams ());

        return layout;
    }

    /**
     * attached to this ListFragment.
     */
    public final T getRefreshListView () {
        return refreshListView;
    }

    /**
     * Returns the  which will replace the ListView created from ListFragment.
     * You should override this method if you wish to customise the  from the default.
     */
    protected abstract T onCreatePullToRefreshListView (LayoutInflater inflater, Bundle savedInstanceState);

}