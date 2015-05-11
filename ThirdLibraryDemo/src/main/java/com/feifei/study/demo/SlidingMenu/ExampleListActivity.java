package com.feifei.study.demo.SlidingMenu;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.feifei.study.R;

public class ExampleListActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen screen,
			Preference pref) {
		Class<?> cls = null;
		String title = pref.getTitle().toString();

		if (title.equals(getString(R.string.properties))) {
			cls = PropertiesActivity.class;
		} else if (title.equals(getString(R.string.responsive_ui))) {
			cls = ResponsiveUIActivity.class;
		} else if (title.equals(getString(R.string.viewpager))) {
			cls = ViewPagerActivity.class;
		}
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		return true;
	}

}
